/**
 * 文章管理
 */
var pageCurr;
var tableIns;
var layedit;
var indexedit;
$(function () {
    layui.use(['table', 'form', 'laydate', 'layedit'], function () {
        var table = layui.table
            , form = layui.form
            , layedit = layui.layedit
            , laydate = layui.laydate;
        layedit.set({
            uploadImage: {
                url: 'http://api.6682828.com/fileupload/texareupload' //layui冨文本图片上传专用接口url
                , type: 'POST' //默认post
            }
        });
        indexedit = layedit.build('eeditor', {
            tool: [
                'strong' //加粗
                , 'italic' //斜体
                , 'underline' //下划线
                , 'del' //删除线
                , '|' //分割线
                , 'left' //左对齐
                , 'center' //居中对齐
                , 'right' //右对齐
                , 'link' //超链接
                , 'unlink' //清除链接
                , 'face' //表情
                , 'image' //插入图片
                , 'help' //帮助
            ]
        });

        tableIns = table.render({
            elem: '#uesrList'
            , url: '/admin/news/list'
            , method: 'get' //默认：get请求
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
                , {field: 'id', title: 'ID', width: '50'}
                , {field: 'title', title: '标題', width: '200'}
                , {field: 'author', title: '专家', width: '120'},
                , {field: 'lotteryid', title: '分类', width: '80', templet: '#showtype'}
                , {field: 'targeturl', title: '链接', width: '200'}
                , {field: 'seq', title: '排序', width: '20'}
                , {field: 'publishflag', title: '是否发布', width: '120', templet: '#publishflag'}
                , {field: 'publishtime', title: '发布时间', width: '150', sort: true}
                , {field: 'createtime', title: '创建时间', width: '150'}
                , {fixed: 'right', title: '操作', width: '100', align: 'center', toolbar: '#optBar1'}

            ]]
            , done: function (res, curr, count) {
                pageCurr = curr;
            }
        });
        // //监听发布操作
        form.on('switch(publishflag)', function (obj) {
            publish(obj, this.value, this.name, obj.elem.checked);
        });
        //监听工具条
        table.on('tool(userTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'edit') {
                //编辑
                editnews(data, data.id);
            }
        });
        //监听查询提交
        form.on('submit(searchSubmit)', function (data) {
            table.reload('tableload', {
                where: {
                    lotteryid: $("#slotteryid").val(),
                    startDate: $("#sstartdate").val(),
                    publishflag: $("#spublishflag").val()
                }
                , page: {
                    curr: pageCurr //从当前页码开始
                }
            });
            return false;
        });
        //监听新建文章提交
        form.on('submit(editSubmit)', function (data) {
            var text = layedit.getContent(indexedit);
            $("#econtent").val(text);
            $("#epublish").val("1");
            var dataa = $("#editnews").serialize();
            $.ajax({
                type: "POST",
                data: dataa,
                url: "/admin/news/create",
                success: function (data) {
                    // console.log("====result=>" + data);
                    layer.alert(data.message, function () {
                        layer.closeAll();
                        //加载load方法
                        load(data);//自定义
                    });
                },
                error: function () {
                    layer.alert("操作请求错误，请您稍后再试", function () {
                        layer.closeAll();
                        //加载load方法
                        //load(obj);//自定义
                    });
                }
            });
            return false;
        });
        //监听保存文章提交
        form.on('submit(resetSubmit)', function (data) {
            var text = layedit.getContent(indexedit);
            $("#econtent").val(text);
            $("#epublish").val("0");
            var dataa = $("#editnews").serialize();
            $.ajax({
                type: "POST",
                data: dataa,
                url: "/admin/news/create",
                success: function (data) {
                    // console.log("====result=>" + data);
                    layer.alert(data.message, function () {
                        layer.closeAll();
                        //加载load方法
                        load(data);//自定义
                    });
                },
                error: function () {
                    layer.alert("操作请求错误，请您稍后再试", function () {
                        layer.closeAll();
                        //加载load方法
                        //load(obj);//自定义
                    });
                }
            });
            return false;
        });
        //日期渲染
        laydate.render({
            elem: '#sstartdate'
        });
    })

});

function publish(obj, value, nameVersion, checked) {
    var isJob = checked ? "1" : "0";
    var userIsJob = checked ? "发布" : "不发布";

    var name = nameVersion.substring(0, nameVersion.indexOf("_"));
    var id = nameVersion.substring(nameVersion.indexOf("_") + 1);
    //是否离职
    layer.confirm('您确定要把文章[' + name + ']设置为' + userIsJob + '状态吗？', {
        btn: ['确认', '返回'] //按钮
    }, function () {
        $.post("/admin/news/publish", {"id": id, "publishflag": isJob}, function (data) {
            if (isLogin(data)) {
                layer.alert(data.message, function () {
                    layer.closeAll();
                    //加载load方法
                    load(obj);//自定义
                });
            }
        });
    });
}

function editnews(obj, id) {
    //回显数据
    $.get("/admin/news/get", {"id": id}, function (d) {
        if (d.status == "200") {
            console.log(d.data);
            //隐藏列表
            $("#newsListLi").removeClass("layui-this");
            $("#newsListDiv").removeClass("layui-show");

            //选中新建
            $("#newseditLi").addClass("layui-this");
            $("#newseditDiv").addClass("layui-show");

            $("#elotteryid").val(d.data.lotteryid);
            $("#etitle").val(d.data.title);
            $("#id").val(d.data.id);
            $("#epublish").val(d.data.publishflag);
            $("#eauthor").val(d.data.author);
            $("#etargeturl").val(d.data.targeturl);
            $("#eseq").val(d.data.seq);
            $("#econtent").val(d.data.content);

            try {
                layui.layedit.setContent(indexedit, d.data.content);
            } catch (e) {
            }
        } else {
            //弹出错误提示
            layer.alert(d.message, function () {
                layer.closeAll();
            });
        }
    })
}

function load(obj) {
    //重新加载table
    tableIns.reload('tableload', {
        where: {
            lotteryid: $("#slotteryid").val(),
            startDate: $("#sstartdate").val(),
            publishflag: $("#spublishflag").val()
        }
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });
}
