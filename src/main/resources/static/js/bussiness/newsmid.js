/**
 * 文章管理
 */
$(function () {
    $("#newsListLi").click(function () {
        $("#newseditLi").removeClass("layui-this");
        $("#newseditDiv").removeClass("layui-show");

        $("#newsListLi").addClass("layui-this");
        $("#newsListDiv").addClass("layui-show");
    });
    $("#newseditLi").click(function () {
        $("#newsListLi").removeClass("layui-this");
        $("#newsListDiv").removeClass("layui-show");

        $("#newseditLi").addClass("layui-this");
        $("#newseditDiv").addClass("layui-show");

    });
});

/**
 * 进入文章管理界面
 */
function load() {
    window.location.href = "/admin/news/toList";
}