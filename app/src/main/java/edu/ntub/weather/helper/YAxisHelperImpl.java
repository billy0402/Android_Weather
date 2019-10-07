package edu.ntub.weather.helper;

import com.github.mikephil.charting.components.YAxis;

import edu.ntub.weather.set.ColorSet;

public class YAxisHelperImpl implements YAxisHelper {
    private YAxis yAxis;

    protected YAxisHelperImpl(YAxis yAxis) {
        this.yAxis = yAxis;
    }

    @Override
    public void setPosition(YAxis.YAxisLabelPosition position) {
        yAxis.setPosition(position);
    }

    @Override
    public void setTextSize(float size) {
        yAxis.setTextSize(size);
    }

    @Override
    public void setDrawGridLineEnable(boolean isEnable) {
        yAxis.setDrawGridLines(isEnable);
    }

    @Override
    public void setGridLineColor(ColorSet colorSet) {
        yAxis.setGridColor(colorSet.getColor());
    }
}
