package edu.ntub.weather.helper;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;

public class LegendHelper {
    private Legend legend;

    private LegendHelper(Chart<?> chart) {
        legend = chart.getLegend();
    }

    public static LegendHelper create(Chart<?> chart) {
        return new LegendHelper(chart);
    }

    public LegendHelper isDrawInside(boolean isDrawInside) {
        legend.setDrawInside(isDrawInside);
        return this;
    }

    public LegendHelper form(Legend.LegendForm form) {
        legend.setForm(form);
        return this;
    }

    public LegendHelper formLineWidth(float width) {
        legend.setFormLineWidth(width);
        return this;
    }

    public LegendHelper verticalAlignment(Legend.LegendVerticalAlignment verticalAlignment) {
        legend.setVerticalAlignment(verticalAlignment);
        return this;
    }

    public LegendHelper horizontalAlignment(Legend.LegendHorizontalAlignment horizontalAlignment) {
        legend.setHorizontalAlignment(horizontalAlignment);
        return this;
    }

    public LegendHelper orientation(Legend.LegendOrientation orientation) {
        legend.setOrientation(orientation);
        return this;
    }

    public LegendHelper textSize(float size) {
        legend.setTextSize(size);
        return this;
    }
}
