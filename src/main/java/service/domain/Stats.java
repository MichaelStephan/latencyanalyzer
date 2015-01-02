package service.domain;

import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by i303874 on 1/2/15.
 */
public class Stats {
    private List<Stat> stats = new LinkedList();

    public void add(Stat stat) {
        checkNotNull(stat);
        this.stats.add(stat);
    }
}
