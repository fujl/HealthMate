package com.mobile.healthmate.manager.report;

import android.content.SharedPreferences;

import com.mobile.healthmate.BuildConfig;
import com.mobile.healthmate.app.App;
import com.mobile.healthmate.app.BaseManager;
import com.mobile.healthmate.app.lib.json.JsonHelper;
import com.mobile.healthmate.manager.user.LoginInfo;
import com.mobile.healthmate.model.HealthTest.HealthReport;
import com.mobile.healthmate.model.online.CmsContentModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by fujl-mac on 2017/8/14.
 * 报告管理
 */

public class ReportManager extends BaseManager {

    private static final String SHARED_REPORT_NAME = "report_info";

    private List<HealthReport> baseReportList;

    public List<HealthReport> reportList;

    @Override
    public void onManagerCreate(App application) {
        loadBaseList();
        loadReport(App.getInstance());
    }

    @Override
    public void onAllManagerCreated() {
        super.onAllManagerCreated();
    }


    private void loadBaseList() {
        try {
            JSONArray jsonArray = new JSONArray(baseJson());
            baseReportList = JsonHelper.toList(jsonArray, HealthReport.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void loadReport(App hcApplication) {
        SharedPreferences sharedPreferences = hcApplication.getSharedPreferences(SHARED_REPORT_NAME, 0);
        String reportsString = sharedPreferences.getString("reports", null);
        if (reportsString == null) {
            reportList = new ArrayList<>();
        } else {
            try {
                JSONObject jsonObject = new JSONObject(reportsString);
                JSONArray jsonArray = jsonObject.getJSONArray("array");
                reportList = JsonHelper.toList(jsonArray, HealthReport.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (reportList == null) {
                if (BuildConfig.DEBUG) {
                    throw new RuntimeException("报告信息读取错误！！！！");
                } else {
                    reportList = new ArrayList<>();
                }
            }
        }
        if (BuildConfig.DEBUG) {
            logger.i("启动，加载报告信息：%s", reportList.toString());
        }
    }

    private void saveReport() {
        if (BuildConfig.DEBUG) {
            logger.i("保存报告信息：%s", reportList.toString());
        }

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(SHARED_REPORT_NAME, 0);
        JSONObject reportJson = JsonHelper.toJSONObject(reportList);
        sharedPreferences.edit().putString("reports", reportJson.toString()).apply();
    }

//    - (BOOL)generateReport:(HMReportModel *)report {
//        BOOL isSuccess = NO;
//        for (HMReportModel *mdl in self.baseReportList) {
//            if ([mdl.age isEqualToString:report.age] && [mdl.gender isEqualToString:report.gender] && [mdl.cold isEqualToString:report.cold] && [mdl.sweating isEqualToString:report.sweating] && [mdl.pain isEqualToString:report.pain] && [mdl.sleep isEqualToString:report.sleep]) {
//                HMReportModel *generate = [HMReportModel copyReport:mdl];
//                generate.generateDt = [[NSDate date] timeIntervalSince1970];
//            [_reportListInfo.reportList addObject:generate];
//
//            [self saveReport];
//            [self loadReport];
//                isSuccess = YES;
//                break;
//            }
//        }
//        return isSuccess;
//    }
    public boolean generateReport(HealthReport healthReport) {
        boolean isSuccess = false;
        for (HealthReport report : baseReportList) {
            if (report.getAge().equals(healthReport.getAge()) && report.getGender().equals(healthReport.getGender()) && report.getCold().equals(healthReport.getCold()) && report.getSweating().equals(healthReport.getCold()) && report.getPain().equals(healthReport.getPain()) && report.getSleep().equals(healthReport.getSleep())) {
                long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date currentDt=new Date(time);
                report.setGenerateDt(format.format(currentDt));
                reportList.add(report);

                saveReport();
                loadReport(App.getInstance());

                isSuccess = true;
                break;
            }
        }
        return isSuccess;
    }

    public boolean randomGenerateReport() {
        int max = baseReportList.size();
        boolean isSuccess = false;
        Random random=new Random();
        int index = random.nextInt(max);
        if (index >= 0 && index < max) {
            HealthReport report = baseReportList.get(index);
            long time=System.currentTimeMillis();
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentDt=new Date(time);
            report.setGenerateDt(format.format(currentDt));
            reportList.add(report);

            saveReport();
            loadReport(App.getInstance());

            isSuccess = true;
        }
        return isSuccess;
    }
//- (BOOL)randomGenerateReport {
//        NSInteger max = self.baseReportList.count;
//        BOOL isSuccess = NO;
//        NSInteger index = arc4random() % max;
//        if (index >= 0 && index < max) {
//            HMReportModel *generate = [self.baseReportList objectAtIndex:index];
//            generate.generateDt = [[NSDate date] timeIntervalSince1970];
//        [_reportListInfo.reportList addObject:generate];
//
//        [self saveReport];
//        [self loadReport];
//            isSuccess = YES;
//        }
//        return isSuccess;
//    }

    private String baseJson() {
        return "[\n" +
                " {\"generate_dt\":\"\",\"sequence_number\":\"1\",\"health_index\":\"90\",\"gender\":\"男\",\"age_group\":\"25-30\",\"cold_case\":\"偏寒\",\"sweating_condition\":\"正常\",\"pain_condition\":\"无\",\"sleep_condition\":\"正常\",\"list\":[{\"organ\":\"胃\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"脾\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"肝\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"胆\",\"index\":\"9.4\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"大肠\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"↑\"},{\"organ\":\"小肠\",\"index\":\"9.1\",\"evaluate\":\"正常\",\"trend\":\"↓\"},{\"organ\":\"肺\",\"index\":\"9.4\",\"evaluate\":\"正常\",\"trend\":\"——\"}]},\n" +
                " {\"generate_dt\":\"\",\"sequence_number\":\"2\",\"health_index\":\"85\",\"gender\":\"女\",\"age_group\":\"40-45\",\"cold_case\":\"偏寒\",\"sweating_condition\":\"正常\",\"pain_condition\":\"无\",\"sleep_condition\":\"正常\",\"list\":[{\"organ\":\"胃\",\"index\":\"8.8\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"脾\",\"index\":\"8.3\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"肝\",\"index\":\"8.8\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"胆\",\"index\":\"9.1\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"大肠\",\"index\":\"9.3\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"小肠\",\"index\":\"9.3\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"肺\",\"index\":\"8.9\",\"evaluate\":\"偏弱\",\"trend\":\"——\"}]},\n" +
                " {\"generate_dt\":\"\",\"sequence_number\":\"3\",\"health_index\":\"81\",\"gender\":\"男\",\"age_group\":\"30-35\",\"cold_case\":\"偏热\",\"sweating_condition\":\"正常\",\"pain_condition\":\"无\",\"sleep_condition\":\"失眠\",\"list\":[{\"organ\":\"胃\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"脾\",\"index\":\"8.2\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"肝\",\"index\":\"8.1\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"胆\",\"index\":\"7.8\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"大肠\",\"index\":\"6.0\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"小肠\",\"index\":\"8.5\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"肺\",\"index\":\"8.5\",\"evaluate\":\"偏弱\",\"trend\":\"——\"}]},\n" +
                " {\"generate_dt\":\"\",\"sequence_number\":\"4\",\"health_index\":\"80\",\"gender\":\"女\",\"age_group\":\"25-30\",\"cold_case\":\"偏寒\",\"sweating_condition\":\"正常\",\"pain_condition\":\"胃疼\",\"sleep_condition\":\"失眠\",\"list\":[{\"organ\":\"胃\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"脾\",\"index\":\"7.5\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"肝\",\"index\":\"6.1\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"胆\",\"index\":\"6.1\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"大肠\",\"index\":\"7.5\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"小肠\",\"index\":\"9.3\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"肺\",\"index\":\"7.5\",\"evaluate\":\"偏弱\",\"trend\":\"——\"}]},\n" +
                " {\"generate_dt\":\"\",\"sequence_number\":\"5\",\"health_index\":\"86\",\"gender\":\"女\",\"age_group\":\"30-35\",\"cold_case\":\"偏热\",\"sweating_condition\":\"正常\",\"pain_condition\":\"无\",\"sleep_condition\":\"正常\",\"list\":[{\"organ\":\"胃\",\"index\":\"8.3\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"脾\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"肝\",\"index\":\"7.6\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"胆\",\"index\":\"8.8\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"大肠\",\"index\":\"8.8\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"小肠\",\"index\":\"9.5\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"肺\",\"index\":\"6.0\",\"evaluate\":\"偏弱\",\"trend\":\"——\"}]},\n" +
                " {\"generate_dt\":\"\",\"sequence_number\":\"6\",\"health_index\":\"79\",\"gender\":\"男\",\"age_group\":\"25-30\",\"cold_case\":\"偏寒\",\"sweating_condition\":\"长出汗\",\"pain_condition\":\"无\",\"sleep_condition\":\"失眠\",\"list\":[{\"organ\":\"胃\",\"index\":\"7.2\",\"evaluate\":\"偏弱\",\"trend\":\"↓\"},{\"organ\":\"脾\",\"index\":\"3.0\",\"evaluate\":\"弱+\",\"trend\":\"↓\"},{\"organ\":\"肝\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"胆\",\"index\":\"9.4\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"大肠\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"↑\"},{\"organ\":\"小肠\",\"index\":\"9.1\",\"evaluate\":\"正常\",\"trend\":\"↑\"},{\"organ\":\"肺\",\"index\":\"3.6\",\"evaluate\":\"弱+\",\"trend\":\"↓\"}]},\n" +
                " {\"generate_dt\":\"\",\"sequence_number\":\"7\",\"health_index\":\"72\",\"gender\":\"女\",\"age_group\":\"20-25\",\"cold_case\":\"偏热\",\"sweating_condition\":\"少出汗\",\"pain_condition\":\"胃疼\",\"sleep_condition\":\"失眠\",\"list\":[{\"organ\":\"胃\",\"index\":\"7.9\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"脾\",\"index\":\"7.3\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"肝\",\"index\":\"8.0\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"胆\",\"index\":\"8.3\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"大肠\",\"index\":\"5.4\",\"evaluate\":\"弱\",\"trend\":\"——\"},{\"organ\":\"小肠\",\"index\":\"9.5\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"肺\",\"index\":\"8.2\",\"evaluate\":\"偏弱\",\"trend\":\"——\"}]},\n" +
                " {\"generate_dt\":\"\",\"sequence_number\":\"8\",\"health_index\":\"88\",\"gender\":\"女\",\"age_group\":\"35-40\",\"cold_case\":\"偏热\",\"sweating_condition\":\"正常\",\"pain_condition\":\"无\",\"sleep_condition\":\"正常\",\"list\":[{\"organ\":\"胃\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"脾\",\"index\":\"7.9\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"肝\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"胆\",\"index\":\"8.3\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"大肠\",\"index\":\"8.3\",\"evaluate\":\"偏弱\",\"trend\":\"——\"},{\"organ\":\"小肠\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"肺\",\"index\":\"9.3\",\"evaluate\":\"正常\",\"trend\":\"——\"}]},\n" +
                " {\"generate_dt\":\"\",\"sequence_number\":\"9\",\"health_index\":\"89\",\"gender\":\"男\",\"age_group\":\"40-45\",\"cold_case\":\"偏热\",\"sweating_condition\":\"正常\",\"pain_condition\":\"无\",\"sleep_condition\":\"正常\",\"list\":[{\"organ\":\"胃\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"↑\"},{\"organ\":\"脾\",\"index\":\"7.9\",\"evaluate\":\"偏弱\",\"trend\":\"↑\"},{\"organ\":\"肝\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"胆\",\"index\":\"8.3\",\"evaluate\":\"偏弱\",\"trend\":\"↑\"},{\"organ\":\"大肠\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"↓\"},{\"organ\":\"小肠\",\"index\":\"6.7\",\"evaluate\":\"偏弱\",\"trend\":\"↓\"},{\"organ\":\"肺\",\"index\":\"9.3\",\"evaluate\":\"正常\",\"trend\":\"↑\"}]},\n" +
                " {\"generate_dt\":\"\",\"sequence_number\":\"10\",\"health_index\":\"86\",\"gender\":\"女\",\"age_group\":\"30-35\",\"cold_case\":\"偏寒\",\"sweating_condition\":\"正常\",\"pain_condition\":\"无\",\"sleep_condition\":\"失眠\",\"list\":[{\"organ\":\"胃\",\"index\":\"8.7\",\"evaluate\":\"偏弱\",\"trend\":\"↓\"},{\"organ\":\"脾\",\"index\":\"7.6\",\"evaluate\":\"偏弱\",\"trend\":\"↓\"},{\"organ\":\"肝\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"↑\"},{\"organ\":\"胆\",\"index\":\"7.8\",\"evaluate\":\"偏弱\",\"trend\":\"↓\"},{\"organ\":\"大肠\",\"index\":\"9.1\",\"evaluate\":\"正常\",\"trend\":\"↑\"},{\"organ\":\"小肠\",\"index\":\"7.8\",\"evaluate\":\"偏弱\",\"trend\":\"↓\"},{\"organ\":\"肺\",\"index\":\"9.1\",\"evaluate\":\"正常\",\"trend\":\"↓\"}]},\n" +
                " {\"generate_dt\":\"\",\"sequence_number\":\"11\",\"health_index\":\"60\",\"gender\":\"男\",\"age_group\":\"35-40\",\"cold_case\":\"偏寒\",\"sweating_condition\":\"少出汗\",\"pain_condition\":\"胸疼\",\"sleep_condition\":\"失眠\",\"list\":[{\"organ\":\"胃\",\"index\":\"3.0\",\"evaluate\":\"弱+\",\"trend\":\"——\"},{\"organ\":\"脾\",\"index\":\"5.0\",\"evaluate\":\"弱\",\"trend\":\"——\"},{\"organ\":\"肝\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"胆\",\"index\":\"9.3\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"大肠\",\"index\":\"3.0\",\"evaluate\":\"弱+\",\"trend\":\"——\"},{\"organ\":\"小肠\",\"index\":\"9.1\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"肺\",\"index\":\"5.3\",\"evaluate\":\"弱\",\"trend\":\"——\"}]},\n" +
                " {\"generate_dt\":\"\",\"sequence_number\":\"12\",\"health_index\":\"83\",\"gender\":\"女\",\"age_group\":\"30-35\",\"cold_case\":\"偏热\",\"sweating_condition\":\"正常\",\"pain_condition\":\"无\",\"sleep_condition\":\"正常\",\"list\":[{\"organ\":\"胃\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"↑\"},{\"organ\":\"脾\",\"index\":\"7.9\",\"evaluate\":\"偏弱\",\"trend\":\"↑\"},{\"organ\":\"肝\",\"index\":\"8.0\",\"evaluate\":\"偏弱\",\"trend\":\"↓\"},{\"organ\":\"胆\",\"index\":\"8.9\",\"evaluate\":\"偏弱\",\"trend\":\"↓\"},{\"organ\":\"大肠\",\"index\":\"7.1\",\"evaluate\":\"偏弱\",\"trend\":\"↑\"},{\"organ\":\"小肠\",\"index\":\"9.5\",\"evaluate\":\"正常\",\"trend\":\"↑\"},{\"organ\":\"肺\",\"index\":\"9.2\",\"evaluate\":\"正常\",\"trend\":\"↓\"}]},\n" +
                " {\"generate_dt\":\"\",\"sequence_number\":\"13\",\"health_index\":\"87\",\"gender\":\"女\",\"age_group\":\"30-35\",\"cold_case\":\"偏寒\",\"sweating_condition\":\"正常\",\"pain_condition\":\"无\",\"sleep_condition\":\"正常\",\"list\":[{\"organ\":\"胃\",\"index\":\"7.6\",\"evaluate\":\"偏弱\",\"trend\":\"↓\"},{\"organ\":\"脾\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"肝\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"胆\",\"index\":\"7.6\",\"evaluate\":\"偏弱\",\"trend\":\"↑\"},{\"organ\":\"大肠\",\"index\":\"7.4\",\"evaluate\":\"偏弱\",\"trend\":\"↓\"},{\"organ\":\"小肠\",\"index\":\"9.2\",\"evaluate\":\"正常\",\"trend\":\"↑\"},{\"organ\":\"肺\",\"index\":\"9.4\",\"evaluate\":\"正常\",\"trend\":\"——\"}]},{\"sequence_number\":\"14\",\"health_index\":\"65\",\"gender\":\"女\",\"age_group\":\"30-35\",\"cold_case\":\"偏热\",\"sweating_condition\":\"长出汗\",\"pain_condition\":\"无\",\"sleep_condition\":\"正常\",\"list\":[{\"organ\":\"胃\",\"index\":\"5.4\",\"evaluate\":\"弱\",\"trend\":\"——\"},{\"organ\":\"脾\",\"index\":\"3.0\",\"evaluate\":\"弱+\",\"trend\":\"——\"},{\"organ\":\"肝\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"胆\",\"index\":\"3.0\",\"evaluate\":\"弱+\",\"trend\":\"——\"},{\"organ\":\"大肠\",\"index\":\"9.2\",\"evaluate\":\"正常\",\"trend\":\"——\"},{\"organ\":\"小肠\",\"index\":\"2.8\",\"evaluate\":\"弱++\",\"trend\":\"——\"},{\"organ\":\"肺\",\"index\":\"5.8\",\"evaluate\":\"弱\",\"trend\":\"——\"}]},\n" +
                " {\"generate_dt\":\"\",\"sequence_number\":\"15\",\"health_index\":\"74\",\"gender\":\"男\",\"age_group\":\"20-25\",\"cold_case\":\"偏热\",\"sweating_condition\":\"长出汗\",\"pain_condition\":\"胃疼\",\"sleep_condition\":\"失眠\",\"list\":[{\"organ\":\"胃\",\"index\":\"6.9\",\"evaluate\":\"偏弱\",\"trend\":\"↓\"},{\"organ\":\"脾\",\"index\":\"9.0\",\"evaluate\":\"正常\",\"trend\":\"↑\"},{\"organ\":\"肝\",\"index\":\"6.9\",\"evaluate\":\"偏弱\",\"trend\":\"↓\"},{\"organ\":\"胆\",\"index\":\"9.3\",\"evaluate\":\"正常\",\"trend\":\"↑\"},{\"organ\":\"大肠\",\"index\":\"3.0\",\"evaluate\":\"弱+\",\"trend\":\"↓\"},{\"organ\":\"小肠\",\"index\":\"3.1\",\"evaluate\":\"弱+\",\"trend\":\"↓\"},{\"organ\":\"肺\",\"index\":\"9.2\",\"evaluate\":\"正常\",\"trend\":\"↑\"}]}\n" +
                " ]";
    }
}
