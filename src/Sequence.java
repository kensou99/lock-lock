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


}
