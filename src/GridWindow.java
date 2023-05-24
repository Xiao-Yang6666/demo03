import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Thread.sleep;

public class GridWindow extends JFrame implements ActionListener {
    private final int GRID_WIDTH = 20;  // 格子的宽度
    private final int GRID_HEIGHT = 40; // 格子的高度
    private final int WINDOW_WIDTH = 630; // 窗口宽度
    private final int WINDOW_HEIGHT = 800; // 窗口高度

    private List<Point> snakeGrid = new ArrayList<>(); // 表示蛇身的格子信息
    GridPanel panel; // 游戏界面画布

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    /**
     * 构建页面
     */
    public GridWindow() {
        setTitle("Snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);

        // 初始化蛇
        snakeGrid.add(new Point(3, 5));
        snakeGrid.add(new Point(2, 5));
        snakeGrid.add(new Point(1, 5));
        snakeGrid.add(new Point(0, 5));

        // 添加自定义的绘制面板
        panel = new GridPanel();
        add(panel);
        // 设置为可见
        setVisible(true);
        // 键盘监听
        addKeyListener(new TAdapter());
        // 保持蛇的移动
        move();
        // 刷页面
        panelRepaint();
    }


    public static void main(String[] args) throws InterruptedException {
//        AtomicReference<GridWindow> gridWindow = new AtomicReference<>();
//        // 初始化开启页面
//        SwingUtilities.invokeLater(() -> {
//            gridWindow.set(new GridWindow());
//            // 初始化蛇的位置
//            gridWindow.get().snakeGrid.add(new Point(3, 5));
//            gridWindow.get().snakeGrid.add(new Point(2, 5));
//            gridWindow.get().snakeGrid.add(new Point(1, 5));
//            gridWindow.get().snakeGrid.add(new Point(0, 5));
//        });
        SwingUtilities.invokeLater(GridWindow::new);
    }

    private synchronized void move() {
        // 保持蛇的移动
        new Thread(() -> {
            while (inGame) {
//                System.out.println(snakeGrid.size());
                // 获取蛇头的位置
                Point point = snakeGrid.get(0);
                if (point != null) {
                    if (leftDirection) {
                        updateSnakeGrid(new Point(point.x - 1, point.y));
                    } else if (rightDirection) {
                        updateSnakeGrid(new Point(point.x + 1, point.y));
                    } else if (upDirection) {
                        updateSnakeGrid(new Point(point.x, point.y - 1));
                    } else if (downDirection) {
                        updateSnakeGrid(new Point(point.x, point.y + 1));
                    }
                }
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }



    /**
     * 绘制游戏页面
     */
    private class GridPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int cellWidth = 20; // 计算每个格子的宽度
            int cellHeight = 20; // 计算每个格子的高度

            // 绘制格子及边框线
            for (int i = 0; i < GRID_HEIGHT; i++) {
                for (int j = 0; j < GRID_WIDTH; j++) {
                    int x = j * cellWidth;
                    int y = i * cellHeight;
                    // 绘制格子
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, cellWidth, cellHeight);
                    // 绘制边框线
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, cellWidth, cellHeight);
                }
            }
            // 绘制蛇
            for (int i = 0; i < snakeGrid.size(); i++) {
                int x = snakeGrid.get(i).x * cellWidth;
                int y = snakeGrid.get(i).y * cellHeight;
                if (i == 0) {
                    g.setColor(Color.RED);
                } else if (i == snakeGrid.size() - 1) {
                    g.setColor(Color.GRAY);
                } else {
                    g.setColor(Color.blue);
                }
                g.fillRect(x, y, cellWidth, cellHeight);
                // 绘制边框线
                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellWidth, cellHeight);
            }

        }
    }

    /**
     * 更新蛇的位置
     * @param newHead 头的位置
     */
    public void updateSnakeGrid(Point newHead) {
        snakeGrid.add(0, newHead); // 将新的蛇头位置添加到蛇身列表的第一个位置
        snakeGrid.remove(snakeGrid.size() - 1); // 移除蛇尾位置，保持蛇身长度不变
    }

    /**
     * 刷新页面
     */
    private void panelRepaint() {
        new Thread(() -> {
            while (true) {
                panel.repaint(); // 重新绘制面板
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
//            checkApple();
//            checkCollision();
//            move();
        }

    }

    /**
     * 键盘监听器
     */
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }





}
