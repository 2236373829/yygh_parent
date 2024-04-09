package com.atguigu.yygh.hosp.controller.api;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.HttpRequestHelper;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.impl.DepartmentServiceImpl;
import com.atguigu.yygh.hosp.service.impl.HospitalServiceImpl;
import com.atguigu.yygh.hosp.service.impl.HospitalSetServiceImpl;
import com.atguigu.yygh.hosp.service.impl.ScheduleServiceImpl;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xyzZero3
 * @create 2024-04-07 22:09
 */
@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalServiceImpl hospitalService;

    @Autowired
    private HospitalSetServiceImpl hospitalSetService;

    @Autowired
    private DepartmentServiceImpl departmentService;

    @Autowired
    private ScheduleServiceImpl scheduleService;

    /**
     * 验证签名方法
     * @param paramMap
     */
    public void validateSignKey(Map<String, Object> paramMap) {
        // 验证签名
        // 1 获取医院系统传递过来的签名，签名进行过MD5加密
        String hospSign = (String) paramMap.get("sign");

        // 2 根据传递过来的医院编码，查询数据库查询签名
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);

        // 3 把数据库查询出来的签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);

        // 4 判断签名是否一致
        if (!hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
    }

    /**
     * 获取模拟接口发送的参数
     * @param request
     * @return
     */
    public Map<String, Object> getParamMap(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        return HttpRequestHelper.switchMap(requestMap);
    }

    // ###########################################医院接口#############################################

    /**
     * 查询医院接口
     * @param request
     * @return
     */
    @PostMapping("/hospital/show")
    public Result getHospital(HttpServletRequest request) {
        // 获取传递过来的医院信息
        Map<String, Object> paramMap = getParamMap(request);

        // 获取医院编号
        String hoscode = (String) paramMap.get("hoscode");

        // 验证签名
        validateSignKey(paramMap);

        // 调用service方法实现根据医院编号查询
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

    /**
     * 上传医院接口
     * @param request
     * @return
     */
    @PostMapping("/saveHospital")
    public Result saveHosp(HttpServletRequest request) {
        // 获取传递过来的医院信息
        Map<String, Object> paramMap = getParamMap(request);
        // 验证签名
        validateSignKey(paramMap);

        // 传输过程中"+"转换为了" "，因此我们要转换回来
        String logoData = (String) paramMap.get("logoData");
        logoData = logoData.replaceAll(" ", "+");
        paramMap.put("logoData", logoData);

        // 调用service的方法
        hospitalService.save(paramMap);
        return Result.ok();
    }



    // ###########################################科室接口#############################################

    /**
     * 上传科室接口
     * @param request
     * @return
     */
    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        // 获取传递过来的科室信息
        Map<String, Object> paramMap = getParamMap(request);
        // 验证签名
        validateSignKey(paramMap);

        departmentService.save(paramMap);
        return Result.ok();
    }

    /**
     * 查询科室接口
     * @param request
     * @return
     */
    @PostMapping("/department/list")
    public Result findDepartment(HttpServletRequest request) {
        // 获取传递过来的查询参数
        Map<String, Object> paramMap = getParamMap(request);

        // 获取医院编号
        String hoscode = (String) paramMap.get("hoscode");
        // 当前页和每页记录数
        Integer page = StringUtils
                .isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
        Integer limit = StringUtils
                .isEmpty(paramMap.get("limit")) ? 10 : Integer.parseInt((String) paramMap.get("limit"));

        validateSignKey(paramMap);

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);

        // 调用service方法
        Page<Department> departmentPage = departmentService.findPageDepartment(page, limit, departmentQueryVo);
        return Result.ok(departmentPage);
    }

    /**
     * 删除科室接口
     * @param request
     * @return
     */
    @PostMapping("/department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        Map<String, Object> paramMap = getParamMap(request);

        // 获取医院编号和科室编号
        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");

        // 签名校验
        validateSignKey(paramMap);

        departmentService.removeDepartment(hoscode, depcode);
        return Result.ok();
    }

    // ###########################################排班接口#############################################

    /**
     * 上传排班接口
     * @return
     */
    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        final Map<String, Object> paramMap = getParamMap(request);

        // 校验签名
        validateSignKey(paramMap);

        scheduleService.saveSchedule(paramMap);
        return Result.ok();

    }

    /**
     * 查询排班接口
     * @param request
     * @return
     */
    @PostMapping("/schedule/list")
    public Result findSchedule(HttpServletRequest request) {
        final Map<String, Object> paramMap = getParamMap(request);

        // 获取医院编号
        String hoscode = (String) paramMap.get("hoscode");
        // 科室编号
        String depcode = (String) paramMap.get("depcode");

        // 当前页和每页记录数
        Integer page = StringUtils
                .isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
        Integer limit = StringUtils
                .isEmpty(paramMap.get("limit")) ? 10 : Integer.parseInt((String) paramMap.get("limit"));

        validateSignKey(paramMap);

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);

        // 调用service方法
        Page<Schedule> schedulePage = scheduleService.findPageSchedule(page, limit, scheduleQueryVo);
        return Result.ok(schedulePage);

    }

    /**
     * 删除排班接口
     * @param request
     * @return
     */
    @PostMapping("/schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        final Map<String, Object> paramMap = getParamMap(request);

        // 获取医院编号和排班id
        String hoscode = (String) paramMap.get("hoscode");
        String hosScheduleId = (String) paramMap.get("hosScheduleId");

        validateSignKey(paramMap);

        scheduleService.removeSchedule(hoscode, hosScheduleId);
        return Result.ok();
    }

}
