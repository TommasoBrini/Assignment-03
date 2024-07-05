package org.ass03.part2A.controller;

import org.ass03.part2A.model.Grid;

public interface GridUpdateListener {
    void onGridCreated();
    void onGridUpdated(int gridIndex);
}