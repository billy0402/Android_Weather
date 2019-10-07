package edu.ntub.weather.helper;

import com.github.mikephil.charting.components.YAxis;

import edu.ntub.weather.set.ColorSet;

public interface YAxisHelper {
    void setPosition(YAxis.YAxisLabelPosition position);

    void setTextSize(float size);

    void setDrawGridLineEnable(boolean isEnable);

    void setGridLineColor(ColorSet colorSet);
}
