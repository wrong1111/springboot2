/**
 * 广告位置管理
 */
var pageCurr;
var tableIns;
$(function () {
    layui.use('table', function () {
        var table = layui.table
            , form = layui.form;

        tableIns = table.render({
            elem: '#uesrList'
            , url: '/admin/issue/list'
            , method: 'get' //默认：get请求
            , cellMinWidth: 80
            , page: true
            , limits: [20, 50, 100]
            , limit: 20
            , where: {
                'lotteryid': $("#slotteryid").val(),
                'startDate': $("#sstartdate").val()
            }
            , id: 'tableload',

            request: {
                pageName: 'pageNo' //页码的参数名称，默认：page
                , limitName: 'pageSize' //每页数据量的参数名，默认：limit
            }, response: {
                statusName: 'code' //数据状态的字段名称，默认：code
                , statusCode: 200 //成功的状态码，默认：0
                , countName: 'totals' //数据总数的字段名称，默认：count
                , dataName: 'list' //数据列表的字段名称，默认：data
            }
            , cols: [[
                {type: 'numbers'}
                , {field: 'id', title: 'ID', unresize: true}
                , {field: 'lotteryname', title: '彩种', unresize: true}
                , {field: 'issueno', title: '期号', unresize: true, sort: true}
                , {field: 'number', title: '开奖号码', unresize: true}
                , {field: 'result', title: '赛果', unresize: true}
                , {
                    field: 'yuceiresult', title: '预测赛果', unresize: true, templet: function (d) {
                        if (d.result != null && d.yuceiresult != null) {
                            var ary = d.yuceiresult.split(",");
                            var show = [];
                            if (ary != null && ary.length > 0) {
                                for (var i = 0; i < ary.length; i++) {
                                    if (d.result.indexOf(ary[i]) > -1) {
                                        show.push("<span style='color: red''>");
                                        show.push(ary[i]);
                                        show.push("</span>");
                                    } else {
                                        show.push(ary[i]);
                                    }
                                    show.push("&nbsp;");
                                }
                                return show.join("");
                            }
                        }
                        return d.yuceiresult == null ? "" : d.yuceiresult;
                    }
                }
                , {
                    field: 'endtime', title: '期结时间', unresize: true, sort: true, templet: function (d) {
                        return DateUtils.formatDate(d.endtime);
                    }
                }
                , {
                    field: 'updatetime', title: '修改时间', unresize: true, align: 'center', templet: function (d) {
                        return d.updatetime == null ? "" : DateUtils.formatDate(d.updatetime);
                    }
                }
                , {
                    field: 'createtime', title: '创建时间', unresize: true, align: 'center', templet: function (d) {
                        return DateUtils.formatDate(d.createtime);
                    }
                }
                , {fixed: 'right', title: '操作', unresize: true, align: 'center', toolbar: '#optBar'}
            ]]
            , done: function (res, curr, count) {
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                //console.log(res);
                //得到当前页码
                //console.log(curr);
                //得到数据总量
                //console.log(count);
                pageCurr = curr;
            }
        });
        // //监听在职操作
        // form.on('switch(isJobTpl)', function (obj) {
        //     setJobUser(obj, this.value, this.name, obj.elem.checked);
        // });
        //监听工具条
        table.on('tool(userTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'edit') {
                //编辑
                editUser(data, data.id);
            }
        });
        //监听提交
        form.on('submit(userSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        //监听提交
        form.on('submit(searchSubmit)', function (data) {
            table.reload('tableload', {
                where: {
                    lotteryid: $("#slotteryid").val(),
                    startDate: $("#sstartdate").val()
                }
                , page: {
                    curr: pageCurr //从当前页码开始
                }
            });
            return false;
        });
        //监听提交，生成某天的期号列表
        form.on('submit(btnbuildissue)', function (data) {
            var dataa = $("#issueForm").serialize();
            $.ajax({
                type: "POST",
                data: dataa,
                url: "/admin/issue/build",
                success: function (data) {
                    layer.alert(data.message, function () {
                        layer.closeAll();
                        //加载页面
                        load(obj);
                    });
                },
                error: function () {
                    layer.alert("操作请求错误，请您稍后再试", function () {
                        layer.closeAll();
                        //加载load方法
                        load(obj);//自定义
                    });
                }
            });
            return false;
        })
    });

    layui.use('laydate', function () {
        var laydate = layui.laydate;
        //常规用法
        laydate.render({
            elem: '#sstartdate'
        });
        laydate.render({
            elem: '#endtime'
            , type: 'datetime'
        });
        laydate.render({
            elem: '#ustartdate'
        })
    })

});

//提交表单
function formSubmit(obj) {
    var currentUser = $("#currentUser").html();
    submitAjax(obj, currentUser);
}

function submitAjax(obj, currentUser) {
    var dataa = $("#userForm").serialize();
    $.ajax({
        type: "POST",
        data: dataa,
        url: "/admin/issue/create",
        success: function (data) {
            if (isLogin(data)) {
                if (data.status == "200") {
                    layer.alert(data.message, function () {
                        if ($("#id").val() == currentUser) {
                            //如果是自己，直接重新登录
                            parent.location.reload();
                        } else {
                            layer.closeAll();
                            //加载页面
                            load(obj);
                        }
                    });
                } else {
                    layer.alert(data.message, function () {
                        layer.closeAll();
                        //加载load方法
                        load(obj);//自定义
                    });
                }
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试", function () {
                layer.closeAll();
                //加载load方法
                load(obj);//自定义
            });
        }
    });
}

function checkRole() {
    return true;
}

//新增加广告位
function addUser() {
    resetPlace();
    openUser(null, "新增广告");
}

function resetPlace() {
    $("#userForm")[0].reset();
}

function buildissue() {
    openissue(null,'生成期号');
}

function openUser(id, title) {
    if (id == null || id == "") {
        $("#id").val("");
    }
    layer.open({
        type: 1,
        title: title,
        fixed: false,
        resize: true,
        shadeClose: true,
        area: ['650px', '500px'],
        content: $('#setUser'),
        end: function () {

        }
    });
}

function openissue(id, title) {
    layer.open({
        type: 1,
        title: title,
        fixed: false,
        resize: true,
        shadeClose: true,
        area: ['500px', '300px'],
        content: $('#buildissueno'),
        end: function () {

        }
    });
}

function editUser(obj, id) {
    //回显数据
    $.get("/admin/issue/get", {"id": id}, function (d) {
        if (d.status == "200") {
            $("#id").val(d.data.id == null ? '' : d.data.id);
            $("#lotteryid").val(d.data.lotteryid == null ? '' : d.data.lotteryid);
            $("#lotteryname").val(d.data.lotteryname == null ? '' : d.data.lotteryname);
            $("#issueno").val(d.data.issueno);
            $("#endtime").val(DateUtils.formatDate(d.data.endtime));
            $("#yuceiresult").val(d.data.yuceiresult == null ? "" : d.data.yuceiresult);
            $("#number").val(d.data.number == null ? "" : d.data.number);
            $("#result").val(d.data.result == null ? "" : d.data.result);

            $("#setUser").find("[name='flag']").each(function (i, item) {
                if ($(item).val() == d.data.flag) {
                    $(item).prop('checked', true);
                    layui.form.render('radio');
                }
            });
            openUser(id, "修改期号记录");
            //重新渲染下form表单中的复选框 否则复选框选中效果无效
            // layui.form.render();
            // layui.form.render('checkbox');
        } else {
            //弹出错误提示
            layer.alert(d.message, function () {
                layer.closeAll();
            });
        }
    })
}

function delUser(obj, id, name) {
    if (null != id) {
        layer.confirm('您确定要删除[' + name + ']广告位吗？', {
            btn: ['确认', '返回'] //按钮
        }, function () {
            $.post("/admin/placecontent/place-del", {"id": id}, function (data) {
                if (data.status == "200") {
                    //回调弹框
                    layer.alert("删除成功！", function () {
                        layer.closeAll();
                        //加载load方法
                        load(obj);//自定义
                    });
                } else {
                    layer.alert(data.message, function () {
                        layer.closeAll();
                        //加载load方法
                        load(obj);//自定义
                    });
                }
            });
        })
    }
}


function load(obj) {
    //重新加载table
    tableIns.reload('tableload', {
        where: {
            lotteryid: $("#slotteryid").val(),
            startDate: $("#sstartdate").val()
        }
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });
}
