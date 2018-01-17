import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class Main extends Application {

    private static GraphicsContext gc;

    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static int CENTRE_X = WINDOW_WIDTH/2;
    private final static int CENTRE_Y = 5*WINDOW_HEIGHT/8;

    private final static int HALF_WIDTH = 50;
    private final static int HALF_HEIGHT = 25;
    private final static int FULL_WIDTH = HALF_WIDTH*2;
    private final static int FULL_HEIGHT = HALF_HEIGHT*2;

    private final static int GRID_RADIUS = 5;

    private static double mouseX = CENTRE_X;
    private static double mouseY = CENTRE_Y;

    private static int[][][] grid = new int[GRID_RADIUS*2+1][GRID_RADIUS*2+1][GRID_RADIUS];

    static {

        Random rnd = new Random(System.currentTimeMillis());

        for (int u = -GRID_RADIUS; u <= GRID_RADIUS; u++) {
            for (int v = -GRID_RADIUS; v <= GRID_RADIUS; v++) {
                grid[u + GRID_RADIUS][v + GRID_RADIUS][0] = 1;
                if (rnd.nextInt(20) == 0) {
                    for (int w = 1; w < GRID_RADIUS; w++) {
                        grid[u + GRID_RADIUS][v + GRID_RADIUS][w] = 1;
                    }
                }
            }
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

        Pane root = new Pane();

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        scene.setOnMouseMoved(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });

        stage.setResizable(false);
        stage.setTitle("Hello World");
        stage.setScene(scene);
        stage.show();

        Canvas canvas = new Canvas();
        canvas.setWidth(WINDOW_WIDTH);
        canvas.setHeight(WINDOW_HEIGHT);
        root.getChildren().add(canvas);

        gc = canvas.getGraphicsContext2D();

        new AnimationTimer() {
            @Override
            public void handle(long now) {

                gc.setFill(Color.BLACK);
                gc.fillRect(0,0, WINDOW_WIDTH, WINDOW_HEIGHT);


                gc.setStroke(Color.GREEN);

                int x1, y1, x2, y2, x3, y3, x4, y4;
                for (int u = -GRID_RADIUS; u <= GRID_RADIUS+1; u++) {
                    for (int v = -GRID_RADIUS; v <= GRID_RADIUS+1; v++) {
                        for (int w = 0; w < GRID_RADIUS; w++) {

                            x1 = CENTRE_X + HALF_WIDTH * u - HALF_WIDTH * v;
                            y1 = CENTRE_Y + HALF_HEIGHT * u + HALF_HEIGHT * v - HALF_HEIGHT*2*w;
                            x2 = CENTRE_X + HALF_WIDTH * (u + 1) - HALF_WIDTH * v;
                            y2 = CENTRE_Y + HALF_HEIGHT * (u + 1) + HALF_HEIGHT * v - HALF_HEIGHT * 2 * w;
                            x3 = CENTRE_X + HALF_WIDTH * (u + 1) - HALF_WIDTH * (v + 1);
                            y3 = CENTRE_Y + HALF_HEIGHT * (u + 1) + HALF_HEIGHT * (v + 1) - HALF_HEIGHT * 2 * w;
                            x4 = CENTRE_X + HALF_WIDTH * u - HALF_WIDTH * (v + 1);
                            y4 = CENTRE_Y + HALF_HEIGHT * u + HALF_HEIGHT * (v + 1) - HALF_HEIGHT * 2 * w;

                            if (w == 0) {

                                if (u <= GRID_RADIUS) {
                                    gc.strokeLine(x1, y1, x2, y2);
                                }

                                if (v <= GRID_RADIUS) {
                                    gc.strokeLine(x1, y1, x4, y4);
                                }
                            }

                            if (u <= GRID_RADIUS && v <= GRID_RADIUS) {

                                if (grid[u + GRID_RADIUS][v + GRID_RADIUS][w] == 1) {

                                    double r0 = GRID_RADIUS;
                                    double q0 = ((mouseY + HALF_HEIGHT*2*r0 - CENTRE_Y) / HALF_HEIGHT - (mouseX - CENTRE_X) / HALF_WIDTH) / 2;
                                    double p0 = (mouseX - CENTRE_X + HALF_WIDTH * q0) / HALF_WIDTH;

                                    double p1 = u;
                                    double q1 = v+1;
                                    double r1 = w-0.5;

                                    double p2 = u+1;
                                    double q2 = v;
                                    double r2 = w-0.5;

                                    gc.setFill(Color.CYAN);
                                    gc.fillPolygon(new double[]{x1, x2, x3, x4}, new double[]{y1, y2, y3, y4}, 4);

                                    if (w > 0) {

                                        //gc.setStroke(Color.WHITE);
                                        //gc.strokeLine(mouseX, mouseY, (x2+x3)/2,(y2+y3)/2+HALF_HEIGHT);
                                        //gc.strokeLine(mouseX, mouseY, (x3+x4)/2,(y3+y4)/2+HALF_HEIGHT);

                                        if (q0 > q1) {
                                            double dx = p1 - p0;
                                            double dy = q1 - q0;
                                            double l = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                                            int z = 64 + (int) (192*(dy * -1) / l);
                                            if (z < 64) z = 64;
                                            if (z > 255) z = 255;
                                            gc.setFill(Color.rgb(z,z,z));
                                        }
                                        else {
                                            gc.setFill(Color.rgb(64,64,64));
                                        }

                                        gc.fillPolygon(new double[]{x3, x4, x4, x3}, new double[]{y3, y4, y4 + FULL_HEIGHT, y3 + FULL_HEIGHT}, 4);

                                        if (p0 > p2) {
                                            double dx = p2 - p0;
                                            double dy = q2 - q0;
                                            double l = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                                            int z = 64 + (int) (192*(dx * -1) / l);
                                            if (z < 64) z = 64;
                                            if (z > 255) z = 255;
                                            gc.setFill(Color.rgb(z,z,z));
                                        }
                                        else {
                                            gc.setFill(Color.rgb(64,64,64));
                                        }

                                        gc.fillPolygon(new double[]{x2, x3, x3, x2}, new double[]{y2, y3, y3 + FULL_HEIGHT, y2 + FULL_HEIGHT}, 4);


                                    }

                                }
                            }

                        }
                    }
                }

            }
        }.start();

    }

    public static void main(String[] args) {
        launch(args);
    }
}