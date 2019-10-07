package edu.ntub.weather.helper;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import edu.ntub.weather.set.ColorSet;

public class AxisHelper {
    private XAxis xAxis;
    private YAxisHelper leftYAxisHelper;
    private YAxisHelper rightYAxisHelper;

    private AxisHelper(Chart<?> chart) {
        xAxis = chart.getXAxis();
        if (chart instanceof BarLineChartBase) {
            BarLineChartBase<?> barLineChartBase = (BarLineChartBase<?>) chart;
            leftYAxisHelper = new YAxisHelperImpl(barLineChartBase.getAxisLeft());
            rightYAxisHelper = new YAxisHelperImpl(barLineChartBase.getAxisRight());
        } else {
            leftYAxisHelper = new EmptyYAxisHelper();
            rightYAxisHelper = new EmptyYAxisHelper();
        }
    }

    public static AxisHelper create(Chart<?> chart) {
        return new AxisHelper(chart);
    }

    public AxisHelper position(XAxis.XAxisPosition position) {
        xAxis.setPosition(position);
        return this;
    }

    public AxisHelper leftYAxisPosition(YAxis.YAxisLabelPosition position) {
        leftYAxisHelper.setPosition(position);
        return this;
    }

    public AxisHelper rightYAxisPosition(YAxis.YAxisLabelPosition position) {
        rightYAxisHelper.setPosition(position);
        return this;
    }

    public AxisHelper textSize(float size) {
        xAxisTextSize(size);
        leftYAxisTextSize(size);
        rightYAxisTextSize(size);
        return this;
    }

    public AxisHelper xAxisTextSize(float size) {
        xAxis.setTextSize(size);
        return this;
    }

    public AxisHelper leftYAxisTextSize(float size) {
        leftYAxisHelper.setTextSize(size);
        return this;
    }

    public AxisHelper rightYAxisTextSize(float size) {
        rightYAxisHelper.setTextSize(size);
        return this;
    }

    public AxisHelper drawGridEnable(boolean isEnable) {
        xAxisDrawGridEnable(isEnable);
        leftYAxisDrawGridEnable(isEnable);
        rightYAxisDrawGridEnable(isEnable);
        return this;
    }

    public AxisHelper xAxisDrawGridEnable(boolean isEnable) {
        xAxis.setDrawGridLines(false);
        return this;
    }

    public AxisHelper leftYAxisDrawGridEnable(boolean isEnable) {
        leftYAxisHelper.setDrawGridLineEnable(isEnable);
        return this;
    }

    public AxisHelper rightYAxisDrawGridEnable(boolean isEnable) {
        rightYAxisHelper.setDrawGridLineEnable(isEnable);
        return this;
    }

    public AxisHelper gridLineColor(ColorSet colorSet) {
        xAxisGridLineColor(colorSet);
        leftYAxisGridLineColor(colorSet);
        rightYAxisGridLineColor(colorSet);
        return this;
    }

    public AxisHelper xAxisGridLineColor(ColorSet colorSet) {
        xAxis.setGridColor(colorSet.getColor());
        return this;
    }

    public AxisHelper leftYAxisGridLineColor(ColorSet colorSet) {
        leftYAxisHelper.setGridLineColor(colorSet);
        return this;
    }

    public AxisHelper rightYAxisGridLineColor(ColorSet colorSet) {
        rightYAxisHelper.setGridLineColor(colorSet);
        return this;
    }
}
