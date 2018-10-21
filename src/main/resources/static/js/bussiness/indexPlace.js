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
            , url: '/admin/placecontent/placecontentlist'
            , method: 'get' //默认：get请求
            , cellMinWidth: 80
            , page: true
            , where: {
                'place': $("#splaceName").val(),
                'placeStatus': $("#splaceStatus").val()
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
                , {field: 'id', title: 'ID', width: 80, unresize: true, sort: true}
                , {field: 'placeName', title: '广告位置', width: 180, unresize: true}
                , {field: 'placeUrl', title: '图片地址',  unresize: true,templet:'#placeUrl'}
                , {field: 'placeStatus', title: '是否有效', width: 180, unresize: true, align: 'center', templet: '#jobTpl'}
                , {field: 'placeType', title: '类型', width: 80, unresize: true, align: 'center',templet:'#placeType'}
                , {field: 'placeSeq', title: '排序', width: 80, unresize: true}
                , {field: 'param', title: '参数', width: 80, unresize: true}
                , {field: 'createtime', title: '创建时间', width: 200, unresize: true, templet: '#createTime'}
                , {fixed: 'right', title: '操作', width: 200, unresize: true, align: 'center', toolbar: '#optBar'}
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
        //监听提交
        form.on('submit(searchSubmit)', function (data) {
            table.reload('tableload', {
                where: {
                    place: data.field.placeName,
                    placeStatus: data.field.placeStatus
                }
                , page: {
                    curr: pageCurr //从当前页码开始
                }
            });
            return false;
        });



    });

    $.ajax({
        type: 'GET',
        url: "/admin/placecontent/list",
        data: {'pageSize':'100'},
        dataType: "json",
        success: function(data){
            if(200 == data.code && data.list != null){
                var options = [];
                var total = data.list.length;
                for(var i =0 ;i< total;i++){
                    var rec = data.list[i];
                    options.push("<option value='");options.push(rec.placeName);options.push("'>");options.push(rec.placeName+"-"+rec.placeDesc);options.push("</option>");
                }
                $("#splaceName").append(options.join(""));
                layui.form.render("select");
            }
        }
    });

    layui.use('upload', function () {
        var $ = layui.jquery
            , upload = layui.upload;

        //普通图片上传
        var uploadInst = upload.render({
            elem: '#placeUrla'
            , url: 'http://api.6682828.com/fileupload/file'  //http://api.6682828.com/fileupload/file
            , accept: 'file' //普通文件
            , exts: 'jpg|jpeg|gif|png' //只允许上传图片
            , before: function (obj) {
                //预读本地文件示例，不支持ie8
                obj.preview(function (index, file, result) {
                    $('#demo1').attr('src', result); //图片链接（base64）
                });
            }
            , done: function (res) {
                //如果上传失败
                if ('200' != res.code ) {
                    return layer.msg(res.message);
                }
                //上传成功
                console.log(res.url);
                $("#placeUrlb").val(res.url);
                $("#demoText").html("");
            }
            , error: function () {
                //演示失败状态，并实现重传
                var demoText = $('#demoText');
                demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
                demoText.find('.demo-reload').on('click', function () {
                    uploadInst.upload();
                });
            }
        });
    })
});


function setJobUser(obj, value, nameVersion, checked) {
    var isJob = checked ? "0" : "1";
    var userIsJob = checked ? "有效" : "无效";

    var name = nameVersion.substring(0, nameVersion.indexOf("_"));
    var id = nameVersion.substring(nameVersion.indexOf("_") + 1);
    //是否离职
    layer.confirm('您确定要把：' + name + '设置为' + userIsJob + '状态吗？', {
        btn: ['确认', '返回'] //按钮
    }, function () {
        $.post("/admin/placecontent/enable-status", {"id": id, "placeStatus": isJob}, function (data) {
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
    $.ajax({
        type: "POST",
        data: dataa,
        url: "/admin/placecontent/create",
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
        area: ['650px', '500px'],
        content: $('#setUser'),
        end: function () {

        }
    });
}

function editUser(obj, id) {
    //回显数据
    $.get("/admin/placecontent/placecontent-get", {"id": id}, function (d) {
        if (d.status == "200") {
            $("#id").val(d.data.id == null ? '' : d.data.id);
            $("#placeName").val(d.data.placeName == null ? '' : d.data.placeName);
            $("#placeDesc").val(d.data.placeDesc == null ? '' : d.data.placeDesc);
            $("#setUser").find("[name='placeType']").each(function(i,item){
                    if($(item).val()== d.data.placeType){
                        $(item).prop('checked',true);
                        layui.form.render('radio');
                    }
            });
            $("#setUser").find("[name='placeStatus']").each(function(i,item){
                if($(item).val()== d.data.placeStatus){
                    $(item).prop('checked',true);
                    layui.form.render('radio');
                }
            });
            if("1" == d.data.placeType){
                $("#demo1").attr("src",d.data.placeUrl);
            }else{
                $("#demo1").attr("src","");
            }
            $("#placeUrlb").val(d.data.placeUrl);
            $("#targeturl").val(d.data.targeturl);
            $("#placeseq").val(d.data.placeSeq == null ? "":d.data.placeSeq);
            $("#param").val(d.data.param);
            openUser(id, "修改广告位");
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
            place: $("#splaceName").val(),
            placeStatus: $("#splaceStatus").val()
        }
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });
}
