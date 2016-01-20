package concurrent.locks;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

/**
 * title: <br/>
 * description: 描述<br/>
 * Copyright: Copyright (c)2014<br/>
 * Company: 易宝支付(YeePay)<br/>
 *
 * @author wenkang.zhang
 * @version 1.0.0
 * @since 16-1-20 上午10:27
 */
public class Sequence {

    public int simulation = 0;

    public int base = 0;

    public int offset = 0;

    public int step;

    public Sequence(int step) {
        this.step = step;
    }

    public int getNext() {
        if (offset < step) {
            return base + offset++;
        }

        base = getSequenceFromDB();
        offset = 0;
        return base + offset++;
    }

    /**
     * 模拟从数据库取一个sequence
     *
     * @return
     */
    public synchronized int getSequenceFromDB() {
        return simulation += step;
    }

    /**
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        //sequence最大值
        final int M = 100000;

        //并发线程数
        int CONCURRENCY = 4;

        //sequence步数
        int STEP = 100;

        //初始化
        final int occur[][] = new int[CONCURRENCY][M];
        for (int i = 0; i < CONCURRENCY; i++) {
            occur[i] = new int[M];
            for (int j = 0; j < M; j++) {
                occur[i][j] = 0;
            }
        }

        final CountDownLatch countDownLatch = new CountDownLatch(CONCURRENCY);
        final long finishTime[] = new long[CONCURRENCY];

        final Sequence s = new Sequence(STEP);
        final long startTime = System.currentTimeMillis();
        for (int i = 0; i < CONCURRENCY; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int j;
                    while ((j = s.getNext()) < M) {
                        occur[finalI][j] = 1;
                    }

                    countDownLatch.countDown();
                    finishTime[finalI] = System.currentTimeMillis() - startTime;
                }
            }).start();
        }

        countDownLatch.await();

        //统计一下结果
        for (int i = 0; i < M; i++) {
            int c = 0;
            for (int j = 0; j < CONCURRENCY; j++) {
                c += occur[j][i];
            }
            if (c == 0) {
                System.out.println("丢号了：" + i);
//                System.exit(0);
            }
            if (c > 1) {
                System.out.println("重号了：" + i);
//                System.exit(0);
            }
        }

        Arrays.sort(finishTime);
        System.out.println("耗时：" + finishTime[CONCURRENCY - 1]);
    }
}
