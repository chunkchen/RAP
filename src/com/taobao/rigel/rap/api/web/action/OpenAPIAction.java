package com.taobao.rigel.rap.api.web.action;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.taobao.rigel.rap.api.service.OpenAPIMgr;
import com.taobao.rigel.rap.common.ActionBase;
import com.taobao.rigel.rap.mock.service.MockMgr;
import com.taobao.rigel.rap.project.bo.Action;
import com.taobao.rigel.rap.project.bo.Project;
import com.taobao.rigel.rap.project.service.ProjectMgr;

public class OpenAPIAction extends ActionBase {

    private static final long serialVersionUID = -1786553279434025468L;

    private OpenAPIMgr openAPIMgr;

    public void setOpenAPIMgr(OpenAPIMgr openAPIMgr) {
        this.openAPIMgr = openAPIMgr;
    }

    private ProjectMgr projectMgr;

    public void setProjectMgr(ProjectMgr projectMgr) {
        this.projectMgr = projectMgr;
    }

    private MockMgr mockMgr;

    public void setMockMgr(MockMgr mockMgr) {
        this.mockMgr = mockMgr;
    }

    private int projectId;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    private int actionId;

    public int getActionId() {
        return this.actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String ver;

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    private String _c;

    private String callback;

    public String get_c() {
        return _c;
    }

    public void set_c(String _c) {
        this._c = _c;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getCallback() {
        return callback;
    }

    public String queryModel() throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Gson g = new Gson();
        resultMap.put("model", openAPIMgr.getModel(projectId, ver));
        resultMap.put("code", 200);
        resultMap.put("msg", "");
        String resultJson = g.toJson(resultMap);

        // JSONP SUPPORTED
        if (callback != null && !callback.isEmpty()) {
            resultJson = (callback + "(" + resultJson + ")");
        } else if (_c != null && !_c.isEmpty()) {
            resultJson = (_c + "(" + resultJson + ")");
        }

        setJson(resultJson);
        return SUCCESS;
    }

    public String querySchema() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Gson g = new Gson();
        resultMap.put("schema",
                openAPIMgr.getSchema(actionId, (type != null && type.equals("request") ? Action.TYPE.REQUEST : Action.TYPE.RESPONSE), ver, projectId));
        resultMap.put("code",
                200);
        resultMap.put("msg", "");
        String resultJson = g.toJson(resultMap);

        // JSONP SUPPORTED
        if (callback != null && !callback.isEmpty()) {
            resultJson = (callback + "(" + resultJson + ")");
        } else if (_c != null && !_c.isEmpty()) {
            resultJson = (_c + "(" + resultJson + ")");
        }

        setJson(resultJson);
        return SUCCESS;
    }

    public String queryRAPModel() throws UnsupportedEncodingException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Gson g = new Gson();
        Project p = projectMgr.getProject(projectId);
        List<Action> aList = p.getAllAction();
        Map<Integer, Object> mockDataMap = new HashMap<Integer, Object>();

        for (Action a : aList) {
            mockDataMap.put(a.getId(), mockMgr.generateRule(a.getId(), null, null));
        }

        resultMap.put("modelJSON", p.toString(Project.TO_STRING_TYPE.TO_PARAMETER));
        resultMap.put("mockjsMap", mockDataMap);
        resultMap.put("code", 200);
        resultMap.put("msg", 0);
        String resultJson = g.toJson(resultMap);

        // JSONP SUPPORTED
        if (callback != null && !callback.isEmpty()) {
            resultJson = (callback + "(" + resultJson + ")");
        } else if (_c != null && !_c.isEmpty()) {
            resultJson = (_c + "(" + resultJson + ")");
        }

        setJson(resultJson);
        return SUCCESS;
    }
}
