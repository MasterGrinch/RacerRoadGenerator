
import constants.SpriteName;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.util.Random;

import static constants.SpriteName.*;


public class RoadGenerator {

    private static final int SEGMENT_LENGTH = 200;
    private static final int TOTAL_CARS = 200;


    private final double LENGTH_NONE = 0;
    private final double LENGTH_SHORT = 25;
    private final double LENGTH_MEDIUM = 50;
    private final double LENGTH_LONG = 100;
    private final double HILL_NONE = 0;
    private final double HILL_LOW = 20;
    private final double HILL_MEDIUM = 40;
    private final double HILL_HIGH = 60;
    private final double CURVE_NONE = 0;
    private final double CURVE_EASY = 2;
    private final double CURVE_MEDIUM = 4;
    private final double CURVE_HARD = 6;


    private JSONArray segments;


    public void createRoad(){
        segments = new JSONArray();

        addStraight(LENGTH_SHORT);
        addSCurves();
        addCurve(LENGTH_MEDIUM, -CURVE_MEDIUM, -HILL_LOW);
        addBumps();
        addLowRollingHills(LENGTH_SHORT,HILL_LOW);
        addCurve(LENGTH_LONG*2, -CURVE_MEDIUM, -HILL_MEDIUM);
        addStraight(LENGTH_MEDIUM);
        addHill(LENGTH_MEDIUM, -HILL_HIGH);
        addSCurves();
        addCurve(LENGTH_LONG, CURVE_MEDIUM, HILL_NONE);
        addHill(LENGTH_LONG, HILL_HIGH);
        addHill(LENGTH_LONG, HILL_HIGH);
        addHill(LENGTH_LONG, HILL_HIGH);
        addCurve(LENGTH_LONG, -CURVE_HARD, HILL_LOW);
        addBumps();
        addHill(LENGTH_LONG, HILL_MEDIUM);
        addSCurves();
        addDownhillToEnd(200.d);


        addSideRoadSprites();
        addCars();

        // write file
        try (FileWriter file = new FileWriter("./src/tracks/track03.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(segments.toJSONString());
            file.flush();

        } catch (Exception e) {
            System.out.println(e);
        }

    }


    public void createV4Final(){

        segments = new JSONArray();


        addStraight(LENGTH_SHORT);
        addLowRollingHills(LENGTH_SHORT,HILL_LOW);
        addSCurves();
        addCurve(LENGTH_MEDIUM, CURVE_MEDIUM, HILL_LOW);
        addBumps();
        addLowRollingHills(LENGTH_SHORT,HILL_LOW);
        addCurve(LENGTH_LONG*2, CURVE_MEDIUM, HILL_MEDIUM);
        addStraight(LENGTH_MEDIUM);
        addHill(LENGTH_MEDIUM, HILL_HIGH);
        addSCurves();
        addCurve(LENGTH_LONG, -CURVE_MEDIUM, HILL_NONE);
        addHill(LENGTH_LONG, HILL_HIGH);
        addCurve(LENGTH_LONG, CURVE_MEDIUM, -HILL_LOW);
        addBumps();
        addHill(LENGTH_LONG, -HILL_MEDIUM);
        addStraight(LENGTH_MEDIUM);
        addSCurves();
        addDownhillToEnd(200.d);

        addSideRoadSprites();
        addCars();

        // write file
        try (FileWriter file = new FileWriter("./src/tracks/track01.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(segments.toJSONString());
            file.flush();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void addSideRoadSprites(){

        int segmentsLength = segments.size();

        addSprite(20,  BILLBOARD07, -1);
        addSprite(40,  BILLBOARD06, -1);
        addSprite(60,  BILLBOARD08, -1);
        addSprite(80,  BILLBOARD09, -1);
        addSprite(100, BILLBOARD01, -1);
        addSprite(120, BILLBOARD02, -1);
        addSprite(140, BILLBOARD03, -1);
        addSprite(160, BILLBOARD04, -1);
        addSprite(180, BILLBOARD05, -1);

        addSprite(240, BILLBOARD07, -1.2);
        addSprite(240, BILLBOARD06,  1.2);
        addSprite(segmentsLength - 25, BILLBOARD07, -1.2);
        addSprite(segmentsLength - 25, BILLBOARD06,  1.2);

        for(int n = 10 ; n < 200 ; n += (int) (4 + (double) (n / 100))) {
            addSprite(n, PALM_TREE, 0.5 + Math.random()*0.5);
            addSprite(n, PALM_TREE,   1 + Math.random()*2);
        }

        for(int n = 250 ; n < 1000 ; n += 5) {
            addSprite(n,     COLUMN, -1.1);
            addSprite(n + rndInt(5), TREE1, -1 - (Math.random() * 2));
            addSprite(n + rndInt(5), TREE2, -1 - (Math.random() * 2));
        }

        for(int n = 200 ; n < segmentsLength ; n += 3) {
            addSprite(n, getRNDTree(), rndSide() * (2 + Math.random() * 5));
        }

        for(int n = 1000 ; n < (segmentsLength - 50) ; n += 100) {
            int side = rndSide();
            addSprite(n + rndInt(50), getRNDBillboard(), -side);
            for(int i = 0 ; i < 20 ; i++) {
                double offset = side * (1.5 + Math.random());
                addSprite(n + rndInt(50), getRNDTree(), offset);
            }
        }
    }

    private void addSprite(int index, SpriteName name, double offset){
        JSONObject sprite = new JSONObject();
        sprite.put("sprite", name.toString());
        sprite.put("offset", offset);


        JSONObject seg = (JSONObject) segments.get(index);
        JSONArray roadside = (JSONArray) seg.get("roadside");
        roadside.add(sprite);
    }

    private void addCars(){
        for(int i = 0; i < TOTAL_CARS; i++){
            double position = Math.floor(Math.random() * segments.size()) * SEGMENT_LENGTH;

            JSONObject car = new JSONObject();
            car.put("sprite", getRNDCar().toString());
            car.put("position", position);
            car.put("offset", getRNDOffset());
            car.put("speed", 3000 + (new Random().nextInt(6000)));

            JSONObject seg = (JSONObject) segments.get((int)(position/SEGMENT_LENGTH)%segments.size());
            JSONArray segCars = (JSONArray) seg.get("cars");
            segCars.add(car);
        }
    }

    private double getRNDOffset(){
        Random rnd = new Random();
        int sign = rnd.nextInt(2);
        int fac = rnd.nextInt(2);

        if(sign == 0){
            fac = -1;
        }
        return fac * 0.8;
    }


    private int rndInt(int bound){
        return new Random().nextInt(bound);
    }

    private int rndSide(){
        Random rnd = new Random();
        int sign = rnd.nextInt(2);
        if(sign == 0){
            return -1;
        }
        return 1;
    }

    private SpriteName getRNDBillboard(){
        SpriteName[] billboards = {BILLBOARD01,BILLBOARD02, BILLBOARD03,BILLBOARD04,BILLBOARD05,BILLBOARD06,BILLBOARD07,BILLBOARD08,BILLBOARD09};
        return billboards[new Random().nextInt(billboards.length - 1)];
    }

    private SpriteName getRNDTree(){
        SpriteName[] trees = {TREE1, TREE2, DEAD_TREE1, DEAD_TREE2, STUMP, BOULDER1, BOULDER2, BOULDER3, BUSH1, BUSH2, CACTUS, PALM_TREE };
        return trees[new Random().nextInt(trees.length - 1)];
    }

    private SpriteName getRNDCar(){
        SpriteName[] cars = {CAR01, CAR02, CAR03, CAR04, SEMI, TRUCK};
        return cars[new Random().nextInt(cars.length - 1)];
    }


    private void addSegment(double curve, double y){
        int n = segments.size();

        int newY = (int)(y * 1000);
        y = (double)newY/1000;

        JSONObject seg = new JSONObject();
        seg.put("index", n);
        seg.put("curve", curve);


        JSONArray p1 = new JSONArray();
        p1.add(0, getLastY());
        p1.add(1, n * SEGMENT_LENGTH);
        JSONArray p2 = new JSONArray();
        p2.add(0, y);
        p2.add(1, (n + 1) * SEGMENT_LENGTH);
        seg.put("p1", p1);
        seg.put("p2", p2);


        JSONArray sprites = new JSONArray();
        JSONArray cars = new JSONArray();
        seg.put("roadside", sprites);
        seg.put("cars", cars);

        segments.add(n, seg);
    }

    private void addRoad(double enter, double hold, double leave, double curve, double y) {
        double startY = getLastY();
        double endY = startY + (y * SEGMENT_LENGTH);
        double total = enter + hold + leave;

        for(int n = 0 ; n < enter ; n++)
            addSegment(easeIn(0, curve, n/enter), easeInOut(startY, endY, n/total));
        for(int n = 0 ; n < hold  ; n++)
            addSegment(curve, easeInOut(startY, endY, (enter+n)/total));
        for(int n = 0 ; n < leave ; n++)
            addSegment(easeInOut(curve, 0, n/leave), easeInOut(startY, endY, (enter+hold+n)/total));
    }

    private double easeIn(double a, double b, double percent){
        return a + (b-a)*Math.pow(percent,2);
    }

    private double easeInOut(double a, double b, double percent){
        return a + (b-a)*((-Math.cos(percent*Math.PI)/2) + 0.5);
    }

    private void addBumps(){
        addRoad(10, 10, 10, 0,  5);
        addRoad(10, 10, 10, 0, -2);
        addRoad(10, 10, 10, 0, -5);
        addRoad(10, 10, 10, 0,  8);
        addRoad(10, 10, 10, 0,  5);
        addRoad(10, 10, 10, 0, -7);
        addRoad(10, 10, 10, 0,  5);
        addRoad(10, 10, 10, 0, -2);
    }

    private void addStraight(Double num){
        num = (num != 0) ? num : LENGTH_MEDIUM;
        addRoad(num,num,num,0,0);
    }

    private void addHill(Double num, Double height){
        num = (num != 0) ? num : LENGTH_MEDIUM;
        height = (height != 0) ? height : HILL_MEDIUM;
        addRoad(num,num,num,0, height);
    }

    private void addCurve(Double num, Double curve, Double height){
        num = (num != 0) ? num : LENGTH_MEDIUM;
        curve = (curve != 0) ? curve : CURVE_MEDIUM;
        height = (height != 0) ? height : HILL_MEDIUM;
        addRoad(num,num,num,curve, height);
    }

    private void addLowRollingHills(Double num, Double height){
        num = (num != 0) ? num : LENGTH_SHORT;
        height = (height != 0) ? height : HILL_LOW;

        addRoad(num, num, num,  0,  height/2);
        addRoad(num, num, num,  0, -height);
        addRoad(num, num, num,  CURVE_EASY,  height);
        addRoad(num, num, num,  0,  0);
        addRoad(num, num, num,  -CURVE_EASY,  height/2);
        addRoad(num, num, num,  0,  0);
    }

    private void addDownhillToEnd(Double num){
        num = (num != 0) ? num : 200;
        addRoad(num,num,num,-CURVE_EASY, -(getLastY()/ SEGMENT_LENGTH));
    }

    // adds S curves to the road
    private void addSCurves(){
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,  -CURVE_EASY, HILL_NONE);
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,   CURVE_MEDIUM, HILL_MEDIUM);
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,   CURVE_EASY, -HILL_LOW);
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,  -CURVE_EASY, HILL_MEDIUM);
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,  -CURVE_MEDIUM, -HILL_MEDIUM);
    }

    private double getLastY(){
        double lastY = 0;
        int size = segments.size();
        if(size != 0){
            JSONObject seg = (JSONObject) segments.get(size-1);
            JSONArray p2 = (JSONArray) seg.get("p2");
            lastY = (double) p2.get(0);
        }
        return lastY;
    }

}