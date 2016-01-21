/**
 * title: <br/>
 * description: 描述<br/>
 * Copyright: Copyright (c)2014<br/>
 * Company: 易宝支付(YeePay)<br/>
 *
 * @author wenkang.zhang
 * @since 16-1-21 下午5:08
 */
public class SequenceByKensou99 extends Sequence {

    public SequenceByKensou99(int step) {
        super(step);
    }

    @Override
    public synchronized int getNext() {
        if (offset < step) {
            return base + offset++;
        }

        base = getSequenceFromDB();
        offset = 0;
        return base + offset++;
    }
}
