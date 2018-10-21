/**
 * 广告位置管理
 */
var pageCurr;
$(function () {
    layui.use('table', function () {
        var table = layui.table
            , form = layui.form;

        tableIns = table.render({
            elem: '#uesrList'
            , url: '/admin/placecontent/list'
            , method: 'get' //默认：get请求
            , cellMinWidth: 80
            , page: true,
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
                , {field: 'id', title: 'ID', width: 80, unresize: true, sort: true}
                , {field: 'placeName', title: '广告位置'}
                , {field: 'placeDesc', title: '描述'}
                , {field: 'placeStatus', title: '是否有效', width: 95, align: 'center', templet: '#jobTpl'}
                , {fixed: 'right', title: '操作', width: 140, align: 'center', toolbar: '#optBar'}
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

        //监听在职操作
        form.on('switch(isJobTpl)', function (obj) {
            setJobUser(obj, this.value, this.name, obj.elem.checked);
        });
        //监听工具条
        table.on('tool(userTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                delUser(data, data.id, data.placeName);
            } else if (obj.event === 'edit') {
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

    });

});

//设置广告位状态有效与否
function setJobUser(obj, value, nameVersion, checked) {
    var isJob = checked ? "0" : "1";
    var userIsJob = checked ? "有效" : "无效";

    var name = nameVersion.substring(0, nameVersion.indexOf("_"));
    var id = nameVersion.substring(nameVersion.indexOf("_") + 1);
    //是否离职
    layer.confirm('您确定要把：' + name + '设置为' + userIsJob + '状态吗？', {
        btn: ['确认', '返回'] //按钮
    }, function () {
        $.post("/admin/placecontent/enable-place-status", {"id": id, "placeStatus": isJob}, function (data) {
            layer.alert(data.message, function () {
                layer.closeAll();
                //加载load方法
                load(obj);//自定义
            });
        });
    });
}

//提交表单
function formSubmit(obj) {
    var currentUser = $("#currentUser").html();
    submitAjax(obj, currentUser);
}

function submitAjax(obj, currentUser) {
    var dataa = $("#userForm").serialize();
    console.log("==>提交数据=" + dataa);
    $.ajax({
        type: "POST",
        data: dataa,
        url: "/admin/placecontent/place-create",
        success: function (data) {
            if (data.status == "200") {
                layer.alert(data.message, function () {
                    if ($("#id").val() == currentUser) {
                        //如果是自己，直接重新登录
                        parent.location.reload();
                    } else {
                        layer.closeAll();
                        cleanUser();
                        //$("#id").val("");
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
    openUser(null, "新增广告位");
}

function resetPlace() {
    $("#userForm")[0].reset();
}

function openUser(id, title) {
    if (id == null || id == "") {
        $("#id").val("");
    }
    layer.open({
        type: 1,
        title: title,
        fixed: false,
        resize: false,
        shadeClose: true,
        area: ['550px'],
        content: $('#edituser'),
        end: function () {
            cleanUser();
        }
    });
}

function editUser(obj, id) {
    //回显数据
    $.get("/admin/placecontent/place-get", {"id": id}, function (d) {
        if (d.status == "200") {
            $("#id").val(d.data.id == null ? '' : d.data.id);
            $("#placeName").val(d.data.placeName == null ? '' : d.data.placeName);
            $("#placeDesc").val(d.data.placeDesc == null ? '' : d.data.placeDesc);
            $("#placeStatusOk").val(d.data.placeStatus == null ? '' : d.data.placeStatus);
            openUser(id, "修改广告位");
            //重新渲染下form表单中的复选框 否则复选框选中效果无效
            // layui.form.render();
            layui.form.render('checkbox');
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

//解锁用户
function nolockUser() {
    //TODO 给个输入框，让用户管理员输入需要解锁的用户手机号，进行解锁操作即可
    layer.alert("TODO");
}

function load(obj) {
    //重新加载table
    tableIns.reload({
        where: obj.field
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });
}

function cleanUser() {

}