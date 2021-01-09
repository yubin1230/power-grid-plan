package com.power.grid.plan.util;


import com.google.common.collect.Lists;
import com.power.grid.plan.dto.bo.NodeBo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.search.*;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.apache.lucene.spatial.query.SpatialArgs;
import org.apache.lucene.spatial.query.SpatialOperation;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Shape;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Lucene位置信息计算
 * @author yubin
 * @date 2021/1/9 20:00
 */
@Component
public class LuceneSpatial {

    private static final Logger LOG = LogManager.getLogger(LuceneSpatial.class);


    /**
     * Spatial4j上下文
     * 1: SpatialContext初始化可由SpatialContextFactory配置
     * 2: SpatialContext属性
     * DistanceCalculator(默认使用GeodesicSphereDistCalc.Haversine,将地球视为标准球体)
     * ShapeFactory(默认使用ShapeFactoryImpl)
     * Rectangle(构建经纬度空间:RectangleImpl(-180, 180, -90, 90, this))
     * BinaryCodec()
     */
    private SpatialContext ctx;

    /**
     * 索引和查询模型的策略接口
     */
    private SpatialStrategy strategy;

    /**
     * 索引存储目录
     */
    private Directory directory;

    @PostConstruct
    private void init() {
        /*
         * SpatialContext也可以通过SpatialContextFactory工厂类来构建
         **/
        this.ctx = SpatialContext.GEO;

        /*
         * 网格最大11层或Geo Hash的精度
         * 1: SpatialPrefixTree定义的Geo Hash最大精度为24
         * 2: GeohashUtils定义类经纬度到Geo Hash值公用方法
         * */
        SpatialPrefixTree spatialPrefixTree = new GeohashPrefixTree(ctx, 11);

        /*
         * 索引和搜索的策略接口,两个主要实现类
         * 1: RecursivePrefixTreeStrategy(支持任何Shape的索引和检索)
         * 2: TermQueryPrefixTreeStrategy(仅支持Point Shape)
         * 上述两个类继承PrefixTreeStrategy(有使用缓存)
         * */
        this.strategy = new RecursivePrefixTreeStrategy(spatialPrefixTree, "location");
        // 初始化索引目录
        this.directory = new RAMDirectory();
    }

    public void createIndex(List<NodeBo> nodeList) throws Exception {
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        IndexWriter indexWriter = new IndexWriter(directory, config);
        indexWriter.addDocuments(newSampleDocument(ctx, strategy, nodeList));
        indexWriter.close();
    }

    /**
     * 创建Document索引对象
     */
    private List<Document> newSampleDocument(SpatialContext ctx, SpatialStrategy strategy, List<NodeBo> nodeList) {
        List<Document> documents = nodeList.stream()
                .map(node -> {
                    Document doc = new Document();
                    doc.add(new StoredField("id", node.getId()));
                    doc.add(new NumericDocValuesField("id", node.getId()));
                    Shape shape;
                    /*
                     * 对小于MaxLevel的Geo Hash构建Field(IndexType[indexed,tokenized,omitNorms])
                     * */
                    Field[] fields = strategy.createIndexableFields((shape = ctx.getShapeFactory()
                            .pointXY(node.getLongitude(), node.getLatitude())));
                    for (Field field : fields) {
                        doc.add(field);
                    }
                    Point pt = (Point) shape;
                    doc.add(new StoredField(strategy.getFieldName(), pt.getX() + "," + pt.getY()));
                    return doc;
                })
                .collect(Collectors.toList());
        return documents;
    }

    /**
     * 地理位置搜索
     */
    public List<NodeBo> search(NodeBo node, double radius) throws IOException {
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        /*
         * 定义坐标点(x,y)即(经度,纬度)即当前用户所在地点(烟台)
         * */
        Point pt = ctx.getShapeFactory().pointXY(node.getLongitude(), node.getLatitude());

        /*
         * 计算当前用户所在坐标点与索引坐标点中心之间的距离即当前用户地点与每个待匹配地点之间的距离,DEG_TO_KM表示以KM为单位
         * 对Field(name=location)字段检索
         * */
        ValueSource valueSource = strategy.makeDistanceValueSource(pt, DistanceUtils.DEG_TO_KM);

        /*
         * 根据命中点与当前位置坐标点的距离远近降序排,距离数字小的排在前面,false表示降序,true表示升序
         * */
        Sort distSort = new Sort(valueSource.getSortField(true)).rewrite(indexSearcher);


        /*
         * 搜索方圆radius千米范围以内,以当前位置经纬度(longitude,latitude)为圆心,其中半径为radiusKM
         * */
        SpatialArgs args = new SpatialArgs(SpatialOperation.Intersects,
                ctx.getShapeFactory().circle(pt, DistanceUtils.dist2Degrees(radius, DistanceUtils.EARTH_MEAN_RADIUS_KM)));
        Query query = strategy.makeQuery(args);
        TopDocs topDocs = indexSearcher.search(query, 50000, distSort);
        /*
         * 输出命中结果
         * */
        List<NodeBo> nodeBoList = transNodeList(topDocs, indexSearcher, args.getShape().getCenter());

        indexReader.close();

        return nodeBoList;
    }

    private List<NodeBo> transNodeList(TopDocs topDocs, IndexSearcher indexSearcher, Point point) throws IOException {
        List<NodeBo> nodeBoList = Lists.newArrayList();
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            int docId = scoreDoc.doc;
            Document document = indexSearcher.doc(docId);
            long nodeId = document.getField("id").numericValue().longValue();
            String location = document.getField(strategy.getFieldName()).stringValue();
            String[] locations = location.split(",");
            double longitude = Double.parseDouble(locations[0]);
            double latitude = Double.parseDouble(locations[1]);
            double distDEG = ctx.calcDistance(point, longitude, latitude);
            double juli = DistanceUtils.degrees2Dist(distDEG, DistanceUtils.EARTH_MEAN_RADIUS_KM);
            LOG.info("docId:{},nodeId:{},longitude:{},latitude{},distance:{}KM", docId, nodeId, longitude, latitude, juli);
            nodeBoList.add(new NodeBo(nodeId, longitude, latitude));
        }
        return nodeBoList;
    }

    public static double GetDistance(double Lat1, double Long1, double Lat2, double Long2){
        double Lat1r = ConvertDegreeToRadians(Lat1);
        double Lat2r = ConvertDegreeToRadians(Lat2);
        double Long1r = ConvertDegreeToRadians(Long1);
        double Long2r = ConvertDegreeToRadians(Long2);

        double R = 6378100; // Earth's radius (km)
        double d = Math.acos(Math.sin(Lat1r) *
                Math.sin(Lat2r) + Math.cos(Lat1r) *
                Math.cos(Lat2r) *
                Math.cos(Long2r-Long1r)) * R;
        return d;
    }

    private static double ConvertDegreeToRadians(double degrees){
        return (Math.PI/180)*degrees;
    }

    public static void main(String[] args) {

        double lng1=114.029055;
        double lat1=22.530542;
        double lng2=114.099164;
        double lat2=22.570018;
        System.out.println(GetDistance(lat1,lng1,lat2,lng2));

    }


}
