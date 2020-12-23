//package api;
//
//import java.lang.reflect.Type;
//import com.google.gson.*;
//
//public class DwGraphJsonDeserializer implements JsonDeserializer<DWGraph_DS> {
//
//    @Override
//    public DWGraph_DS deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
//        JsonObject jsonObject = json.getAsJsonObject();
//        directed_weighted_graph ret = new DWGraph_DS();
//        JsonArray edges = jsonObject.get("Edges").getAsJsonArray();
//        JsonArray nodes = jsonObject.get("Nodes").getAsJsonArray();
//        for (JsonElement node : nodes) {
//            JsonObject jasonNode = node.getAsJsonObject();
//            Integer key = jasonNode.get("id").getAsInt();
//            String[] tempArr = jasonNode.getAsJsonPrimitive("pos").getAsString().split(",");
//            Double[] posArr = new Double[tempArr.length];
//            for (int i = 0; i < tempArr.length; i++) {
//                posArr[i] = Double.parseDouble(tempArr[i]);
//            }
//            geo_location tempLoc = new NodeData.GeoLocation(posArr[0], posArr[1], posArr[2]);
//            node_data tempNode = new NodeData(key);
//            tempNode.setLocation(tempLoc);
//            ret.addNode(tempNode);
//        }
//        for (JsonElement edge : edges) {
//            JsonObject jasonEdge = edge.getAsJsonObject();
//            Integer src = jasonEdge.get("src").getAsInt();
//            Integer dest = jasonEdge.get("dest").getAsInt();
//            Double weight = jasonEdge.get("w").getAsDouble();
//            ret.connect(src, dest, weight);
//        }
//        return (DWGraph_DS) ret;
//
//
//    }
//}
//
package api;

import com.google.gson.*;

import java.lang.reflect.Type;
/**
 * Method To deserialize details from a structure of a JSON file.
 */
public class DwGraphJsonDeserializer implements JsonDeserializer<DWGraph_DS> {
    @Override
    public DWGraph_DS deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        directed_weighted_graph g = new DWGraph_DS();
        JsonElement GraphNodes = jsonObject.get("Nodes");
        JsonArray nodesJson = GraphNodes.getAsJsonArray();
        for (JsonElement tmpNode : nodesJson) {
            JsonElement jasonValueElement = tmpNode.getAsJsonObject();
            int NodeKey = jasonValueElement.getAsJsonObject().get("id").getAsInt();
            String NodePos = jasonValueElement.getAsJsonObject().get("pos").getAsString();
            String[] PosArr = NodePos.split(",");
            double[] DoublePos = new double[3];
            for (int i = 0; i < PosArr.length; i++) {
                DoublePos[i] = Double.parseDouble(PosArr[i]);
            }
            node_data n = new NodeData(DoublePos[0], DoublePos[1], DoublePos[2], NodeKey);
            g.addNode(n);
            }
            JsonArray edgesJson = jsonObject.get("Edges").getAsJsonArray();
            for (JsonElement tmpEdge : edgesJson) {
                JsonElement tmpEd = tmpEdge.getAsJsonObject();
                int EdSrc = tmpEd.getAsJsonObject().get("src").getAsInt();
                int EdDest = tmpEd.getAsJsonObject().get("dest").getAsInt();
                double EdWeight = tmpEd.getAsJsonObject().get("w").getAsDouble();
                g.connect(EdSrc, EdDest, EdWeight);
            }
        return (DWGraph_DS) g;
    }
}