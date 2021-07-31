/**
 * get 请求
 * @param url 请求地址
 * @param params 参数
 * @param callback 回调函数
 */
function get(url, params, callback) {
    $.get(url, params, function (response) {
        callback(toDealWith(response));
    });
}

/**
 * get 请求
 * @param url 请求地址
 * @param callback 回调函数
 */
function getRest(url, callback) {
    $.get(url, function (response) {
        callback(toDealWith(response));
    });
}

/**
 * post 请求
 * @param url 请求地址
 * @param params 参数
 * @param callback 回调函数
 */
function post(url, params, callback) {
    $.post(url, params, function (response) {
        callback(toDealWith(response));
    });
}

/**
 * 上传文件
 * @param url 请求地址
 * @param file 文件
 */
function uploadFile(url, file) {

    var fd = new FormData();
    fd.append('file', file);
    $.ajax({
        type: 'POST',
        url: url,
        data: fd,
        contentType: false,
        processData: false,
        success: function (result) {
            console.log(result);
        },
        error: function (err) {
            console.log(err);
        }
    });
}

function put(url, body, callback) {
    $.ajax({
        url: url,
        type: 'PUT',
        data: JSON.stringify(body),
        contentType: "application/json",
        dataType: "json",
        success: function (response) {
            callback(toDealWith(response));
        }
    });
}

function remove(url, param, callback) {
    $.ajax({
        url: url + "/" + param,
        type: 'DELETE',
        success: function (result) {
            callback(result.code === 0 ? 0 : -1);
        },
        error: function (result) {
            callback(-1);
        }
    });
}

function toDealWith(response) {

    if (response.code === 0) {
        return response.data;
    }
    console.log(response.data);

    return null;
}

























