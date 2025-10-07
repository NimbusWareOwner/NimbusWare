package com.example.nimbusware.analytics;

import com.example.nimbusware.utils.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Advanced analytics system for comprehensive data analysis
 */
public class AdvancedAnalytics {
    private static volatile AdvancedAnalytics instance;
    private final Map<String, DataPoint> dataPoints = new ConcurrentHashMap<>();
    private final Map<String, TrendAnalysis> trends = new ConcurrentHashMap<>();
    private final Map<String, PerformanceMetric> performanceMetrics = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3, r -> {
        Thread t = new Thread(r, "AdvancedAnalytics-Thread");
        t.setDaemon(true);
        return t;
    });
    
    private final AtomicLong totalDataPoints = new AtomicLong(0);
    private final long dataRetentionDays = 30;
    
    private AdvancedAnalytics() {
        startAnalyticsProcessing();
        Logger.info("Advanced Analytics initialized");
    }
    
    public static AdvancedAnalytics getInstance() {
        if (instance == null) {
            synchronized (AdvancedAnalytics.class) {
                if (instance == null) {
                    instance = new AdvancedAnalytics();
                }
            }
        }
        return instance;
    }
    
    /**
     * Record a data point
     * @param metricName Metric name
     * @param value Value
     * @param category Category
     * @param tags Additional tags
     */
    public void recordDataPoint(String metricName, double value, String category, Map<String, String> tags) {
        DataPoint dataPoint = new DataPoint(metricName, value, category, tags, System.currentTimeMillis());
        dataPoints.put(dataPoint.getId(), dataPoint);
        totalDataPoints.incrementAndGet();
        
        // Update performance metrics
        updatePerformanceMetric(metricName, value);
        
        Logger.debug("Recorded data point: " + metricName + " = " + value);
    }
    
    /**
     * Record a simple data point
     * @param metricName Metric name
     * @param value Value
     */
    public void recordDataPoint(String metricName, double value) {
        recordDataPoint(metricName, value, "general", new HashMap<>());
    }
    
    /**
     * Record module usage
     * @param moduleName Module name
     * @param duration Duration in milliseconds
     * @param success Whether operation was successful
     */
    public void recordModuleUsage(String moduleName, long duration, boolean success) {
        Map<String, String> tags = new HashMap<>();
        tags.put("module", moduleName);
        tags.put("success", String.valueOf(success));
        
        recordDataPoint("module.usage", duration, "module", tags);
        recordDataPoint("module.success_rate", success ? 1.0 : 0.0, "module", tags);
    }
    
    /**
     * Record performance metric
     * @param metricName Metric name
     * @param value Value
     * @param unit Unit of measurement
     */
    public void recordPerformanceMetric(String metricName, double value, String unit) {
        Map<String, String> tags = new HashMap<>();
        tags.put("unit", unit);
        
        recordDataPoint("performance." + metricName, value, "performance", tags);
    }
    
    /**
     * Record user action
     * @param action Action name
     * @param context Action context
     * @param duration Duration in milliseconds
     */
    public void recordUserAction(String action, String context, long duration) {
        Map<String, String> tags = new HashMap<>();
        tags.put("action", action);
        tags.put("context", context);
        
        recordDataPoint("user.action", duration, "user", tags);
    }
    
    /**
     * Get trend analysis for a metric
     * @param metricName Metric name
     * @param timeRange Time range in hours
     * @return Trend analysis
     */
    public TrendAnalysis getTrendAnalysis(String metricName, int timeRange) {
        long cutoffTime = System.currentTimeMillis() - (timeRange * 60 * 60 * 1000L);
        
        List<DataPoint> relevantData = dataPoints.values().stream()
            .filter(dp -> dp.getMetricName().equals(metricName))
            .filter(dp -> dp.getTimestamp() >= cutoffTime)
            .sorted(Comparator.comparing(DataPoint::getTimestamp))
            .collect(Collectors.toList());
        
        if (relevantData.isEmpty()) {
            return new TrendAnalysis(metricName, 0, 0, 0, 0, TrendDirection.STABLE);
        }
        
        return calculateTrend(relevantData);
    }
    
    /**
     * Get performance report
     * @param timeRange Time range in hours
     * @return Performance report
     */
    public PerformanceReport getPerformanceReport(int timeRange) {
        long cutoffTime = System.currentTimeMillis() - (timeRange * 60 * 60 * 1000L);
        
        Map<String, List<DataPoint>> dataByCategory = dataPoints.values().stream()
            .filter(dp -> dp.getTimestamp() >= cutoffTime)
            .collect(Collectors.groupingBy(DataPoint::getCategory));
        
        PerformanceReport report = new PerformanceReport();
        
        for (Map.Entry<String, List<DataPoint>> entry : dataByCategory.entrySet()) {
            String category = entry.getKey();
            List<DataPoint> data = entry.getValue();
            
            if (!data.isEmpty()) {
                double avg = data.stream().mapToDouble(DataPoint::getValue).average().orElse(0.0);
                double max = data.stream().mapToDouble(DataPoint::getValue).max().orElse(0.0);
                double min = data.stream().mapToDouble(DataPoint::getValue).min().orElse(0.0);
                
                report.addCategoryStats(category, avg, max, min, data.size());
            }
        }
        
        return report;
    }
    
    /**
     * Get module analytics
     * @param moduleName Module name
     * @param timeRange Time range in hours
     * @return Module analytics
     */
    public ModuleAnalytics getModuleAnalytics(String moduleName, int timeRange) {
        long cutoffTime = System.currentTimeMillis() - (timeRange * 60 * 60 * 1000L);
        
        List<DataPoint> moduleData = dataPoints.values().stream()
            .filter(dp -> dp.getTags().containsKey("module"))
            .filter(dp -> dp.getTags().get("module").equals(moduleName))
            .filter(dp -> dp.getTimestamp() >= cutoffTime)
            .collect(Collectors.toList());
        
        if (moduleData.isEmpty()) {
            return new ModuleAnalytics(moduleName, 0, 0, 0, 0, 0, 0);
        }
        
        // Calculate usage statistics
        long totalUsage = moduleData.stream()
            .filter(dp -> dp.getMetricName().equals("module.usage"))
            .mapToLong(dp -> (long) dp.getValue())
            .sum();
        
        long usageCount = moduleData.stream()
            .filter(dp -> dp.getMetricName().equals("module.usage"))
            .count();
        
        double successRate = moduleData.stream()
            .filter(dp -> dp.getMetricName().equals("module.success_rate"))
            .mapToDouble(DataPoint::getValue)
            .average()
            .orElse(0.0);
        
        double avgUsage = usageCount > 0 ? (double) totalUsage / usageCount : 0.0;
        
        return new ModuleAnalytics(moduleName, totalUsage, usageCount, avgUsage, successRate, 
                                 moduleData.size(), cutoffTime);
    }
    
    /**
     * Get user behavior analytics
     * @param timeRange Time range in hours
     * @return User behavior analytics
     */
    public UserBehaviorAnalytics getUserBehaviorAnalytics(int timeRange) {
        long cutoffTime = System.currentTimeMillis() - (timeRange * 60 * 60 * 1000L);
        
        List<DataPoint> userData = dataPoints.values().stream()
            .filter(dp -> dp.getCategory().equals("user"))
            .filter(dp -> dp.getTimestamp() >= cutoffTime)
            .collect(Collectors.toList());
        
        Map<String, Long> actionCounts = userData.stream()
            .filter(dp -> dp.getMetricName().equals("user.action"))
            .collect(Collectors.groupingBy(
                dp -> dp.getTags().get("action"),
                Collectors.counting()
            ));
        
        Map<String, Double> avgActionDuration = userData.stream()
            .filter(dp -> dp.getMetricName().equals("user.action"))
            .collect(Collectors.groupingBy(
                dp -> dp.getTags().get("action"),
                Collectors.averagingDouble(DataPoint::getValue)
            ));
        
        return new UserBehaviorAnalytics(actionCounts, avgActionDuration, userData.size());
    }
    
    /**
     * Generate insights
     * @param timeRange Time range in hours
     * @return List of insights
     */
    public List<Insight> generateInsights(int timeRange) {
        List<Insight> insights = new ArrayList<>();
        
        // Analyze trends
        for (String metric : getUniqueMetrics()) {
            TrendAnalysis trend = getTrendAnalysis(metric, timeRange);
            if (trend.getDirection() != TrendDirection.STABLE) {
                insights.add(new Insight(
                    "Trend Analysis",
                    metric + " is " + trend.getDirection().toString().toLowerCase() + 
                    " (change: " + String.format("%.2f", trend.getChange()) + ")",
                    InsightType.TREND,
                    trend.getConfidence()
                ));
            }
        }
        
        // Analyze performance
        PerformanceReport report = getPerformanceReport(timeRange);
        for (Map.Entry<String, CategoryStats> entry : report.getCategoryStats().entrySet()) {
            CategoryStats stats = entry.getValue();
            if (stats.getAverage() > stats.getMaximum() * 0.8) {
                insights.add(new Insight(
                    "Performance Alert",
                    entry.getKey() + " is running at " + 
                    String.format("%.1f", stats.getAverage()) + " (high usage)",
                    InsightType.PERFORMANCE,
                    0.8
                ));
            }
        }
        
        // Analyze module usage
        for (String module : getModules()) {
            ModuleAnalytics analytics = getModuleAnalytics(module, timeRange);
            if (analytics.getSuccessRate() < 0.5) {
                insights.add(new Insight(
                    "Module Issue",
                    module + " has low success rate: " + 
                    String.format("%.1f", analytics.getSuccessRate() * 100) + "%",
                    InsightType.MODULE,
                    0.7
                ));
            }
        }
        
        return insights.stream()
            .sorted(Comparator.comparing(Insight::getConfidence).reversed())
            .collect(Collectors.toList());
    }
    
    private void updatePerformanceMetric(String metricName, double value) {
        PerformanceMetric metric = performanceMetrics.computeIfAbsent(metricName, 
            k -> new PerformanceMetric(metricName));
        metric.update(value);
    }
    
    private TrendAnalysis calculateTrend(List<DataPoint> data) {
        if (data.size() < 2) {
            return new TrendAnalysis(data.get(0).getMetricName(), 0, 0, 0, 0, TrendDirection.STABLE);
        }
        
        double firstValue = data.get(0).getValue();
        double lastValue = data.get(data.size() - 1).getValue();
        double change = lastValue - firstValue;
        double changePercent = firstValue != 0 ? (change / firstValue) * 100 : 0;
        
        double sum = data.stream().mapToDouble(DataPoint::getValue).sum();
        double avg = sum / data.size();
        
        double variance = data.stream()
            .mapToDouble(dp -> Math.pow(dp.getValue() - avg, 2))
            .average()
            .orElse(0.0);
        double stdDev = Math.sqrt(variance);
        
        TrendDirection direction = TrendDirection.STABLE;
        if (Math.abs(changePercent) > 10) {
            direction = changePercent > 0 ? TrendDirection.INCREASING : TrendDirection.DECREASING;
        }
        
        double confidence = Math.min(1.0, Math.abs(changePercent) / 50.0);
        
        return new TrendAnalysis(data.get(0).getMetricName(), change, changePercent, 
                               avg, stdDev, direction, confidence);
    }
    
    private Set<String> getUniqueMetrics() {
        return dataPoints.values().stream()
            .map(DataPoint::getMetricName)
            .collect(Collectors.toSet());
    }
    
    private Set<String> getModules() {
        return dataPoints.values().stream()
            .filter(dp -> dp.getTags().containsKey("module"))
            .map(dp -> dp.getTags().get("module"))
            .collect(Collectors.toSet());
    }
    
    private void startAnalyticsProcessing() {
        // Clean old data every hour
        scheduler.scheduleAtFixedRate(() -> {
            try {
                cleanOldData();
            } catch (Exception e) {
                Logger.error("Error cleaning old data", e);
            }
        }, 1, 1, TimeUnit.HOURS);
        
        // Process trends every 5 minutes
        scheduler.scheduleAtFixedRate(() -> {
            try {
                processTrends();
            } catch (Exception e) {
                Logger.error("Error processing trends", e);
            }
        }, 5, 5, TimeUnit.MINUTES);
    }
    
    private void cleanOldData() {
        long cutoffTime = System.currentTimeMillis() - (dataRetentionDays * 24 * 60 * 60 * 1000L);
        
        int removedCount = 0;
        Iterator<Map.Entry<String, DataPoint>> iterator = dataPoints.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, DataPoint> entry = iterator.next();
            if (entry.getValue().getTimestamp() < cutoffTime) {
                iterator.remove();
                removedCount++;
            }
        }
        
        if (removedCount > 0) {
            Logger.info("Cleaned " + removedCount + " old data points");
        }
    }
    
    private void processTrends() {
        for (String metric : getUniqueMetrics()) {
            TrendAnalysis trend = getTrendAnalysis(metric, 24);
            trends.put(metric, trend);
        }
    }
    
    /**
     * Get analytics statistics
     * @return Analytics statistics
     */
    public AnalyticsStatistics getStatistics() {
        return new AnalyticsStatistics(
            totalDataPoints.get(),
            dataPoints.size(),
            performanceMetrics.size(),
            trends.size()
        );
    }
    
    /**
     * Shutdown the analytics system
     */
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    // Data classes
    public static class DataPoint {
        private final String id;
        private final String metricName;
        private final double value;
        private final String category;
        private final Map<String, String> tags;
        private final long timestamp;
        
        public DataPoint(String metricName, double value, String category, 
                        Map<String, String> tags, long timestamp) {
            this.id = UUID.randomUUID().toString();
            this.metricName = metricName;
            this.value = value;
            this.category = category;
            this.tags = new HashMap<>(tags);
            this.timestamp = timestamp;
        }
        
        public String getId() { return id; }
        public String getMetricName() { return metricName; }
        public double getValue() { return value; }
        public String getCategory() { return category; }
        public Map<String, String> getTags() { return tags; }
        public long getTimestamp() { return timestamp; }
    }
    
    public static class TrendAnalysis {
        private final String metricName;
        private final double change;
        private final double changePercent;
        private final double average;
        private final double standardDeviation;
        private final TrendDirection direction;
        private final double confidence;
        
        public TrendAnalysis(String metricName, double change, double changePercent, 
                           double average, double standardDeviation, TrendDirection direction) {
            this(metricName, change, changePercent, average, standardDeviation, direction, 0.5);
        }
        
        public TrendAnalysis(String metricName, double change, double changePercent, 
                           double average, double standardDeviation, TrendDirection direction, double confidence) {
            this.metricName = metricName;
            this.change = change;
            this.changePercent = changePercent;
            this.average = average;
            this.standardDeviation = standardDeviation;
            this.direction = direction;
            this.confidence = confidence;
        }
        
        public String getMetricName() { return metricName; }
        public double getChange() { return change; }
        public double getChangePercent() { return changePercent; }
        public double getAverage() { return average; }
        public double getStandardDeviation() { return standardDeviation; }
        public TrendDirection getDirection() { return direction; }
        public double getConfidence() { return confidence; }
    }
    
    public enum TrendDirection {
        INCREASING, DECREASING, STABLE
    }
    
    public static class PerformanceMetric {
        private final String name;
        private double sum = 0;
        private long count = 0;
        private double min = Double.MAX_VALUE;
        private double max = Double.MIN_VALUE;
        
        public PerformanceMetric(String name) {
            this.name = name;
        }
        
        public void update(double value) {
            sum += value;
            count++;
            min = Math.min(min, value);
            max = Math.max(max, value);
        }
        
        public String getName() { return name; }
        public double getAverage() { return count > 0 ? sum / count : 0; }
        public double getMin() { return min == Double.MAX_VALUE ? 0 : min; }
        public double getMax() { return max == Double.MIN_VALUE ? 0 : max; }
        public long getCount() { return count; }
    }
    
    public static class PerformanceReport {
        private final Map<String, CategoryStats> categoryStats = new HashMap<>();
        
        public void addCategoryStats(String category, double average, double max, double min, int count) {
            categoryStats.put(category, new CategoryStats(average, max, min, count));
        }
        
        public Map<String, CategoryStats> getCategoryStats() { return categoryStats; }
    }
    
    public static class CategoryStats {
        private final double average;
        private final double maximum;
        private final double minimum;
        private final int count;
        
        public CategoryStats(double average, double maximum, double minimum, int count) {
            this.average = average;
            this.maximum = maximum;
            this.minimum = minimum;
            this.count = count;
        }
        
        public double getAverage() { return average; }
        public double getMaximum() { return maximum; }
        public double getMinimum() { return minimum; }
        public int getCount() { return count; }
    }
    
    public static class ModuleAnalytics {
        private final String moduleName;
        private final long totalUsage;
        private final long usageCount;
        private final double averageUsage;
        private final double successRate;
        private final int dataPoints;
        private final long timeRange;
        
        public ModuleAnalytics(String moduleName, long totalUsage, long usageCount, 
                             double averageUsage, double successRate, int dataPoints, long timeRange) {
            this.moduleName = moduleName;
            this.totalUsage = totalUsage;
            this.usageCount = usageCount;
            this.averageUsage = averageUsage;
            this.successRate = successRate;
            this.dataPoints = dataPoints;
            this.timeRange = timeRange;
        }
        
        public String getModuleName() { return moduleName; }
        public long getTotalUsage() { return totalUsage; }
        public long getUsageCount() { return usageCount; }
        public double getAverageUsage() { return averageUsage; }
        public double getSuccessRate() { return successRate; }
        public int getDataPoints() { return dataPoints; }
        public long getTimeRange() { return timeRange; }
    }
    
    public static class UserBehaviorAnalytics {
        private final Map<String, Long> actionCounts;
        private final Map<String, Double> avgActionDuration;
        private final int totalActions;
        
        public UserBehaviorAnalytics(Map<String, Long> actionCounts, 
                                   Map<String, Double> avgActionDuration, int totalActions) {
            this.actionCounts = actionCounts;
            this.avgActionDuration = avgActionDuration;
            this.totalActions = totalActions;
        }
        
        public Map<String, Long> getActionCounts() { return actionCounts; }
        public Map<String, Double> getAvgActionDuration() { return avgActionDuration; }
        public int getTotalActions() { return totalActions; }
    }
    
    public static class Insight {
        private final String title;
        private final String description;
        private final InsightType type;
        private final double confidence;
        
        public Insight(String title, String description, InsightType type, double confidence) {
            this.title = title;
            this.description = description;
            this.type = type;
            this.confidence = confidence;
        }
        
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public InsightType getType() { return type; }
        public double getConfidence() { return confidence; }
    }
    
    public enum InsightType {
        TREND, PERFORMANCE, MODULE, USER, SECURITY
    }
    
    public static class AnalyticsStatistics {
        private final long totalDataPoints;
        private final int activeDataPoints;
        private final int performanceMetrics;
        private final int trends;
        
        public AnalyticsStatistics(long totalDataPoints, int activeDataPoints, 
                                 int performanceMetrics, int trends) {
            this.totalDataPoints = totalDataPoints;
            this.activeDataPoints = activeDataPoints;
            this.performanceMetrics = performanceMetrics;
            this.trends = trends;
        }
        
        public long getTotalDataPoints() { return totalDataPoints; }
        public int getActiveDataPoints() { return activeDataPoints; }
        public int getPerformanceMetrics() { return performanceMetrics; }
        public int getTrends() { return trends; }
    }
}