function ajaxHandle(url, type, data, callback) {
    $.ajax({
        url: url,
        dataType: "json",
        async: true,
        data: data,
        type: type,
        beforeSend: function () {
            layerIndex = layer.load(0, {
                shade: false
            });
        },
        success: function (data, textStatus) {
            layer.close(layerIndex);
            if (data.success) {
                callback(data)
            } else {
                layer.msg(data.msg);
            }

        },
        error: function () {
            layer.close(layerIndex);
            layer.msg('网络错误');
        }
    });
}