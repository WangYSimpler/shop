
var baseRequest={

    sendGet: function (url, params, async, callback) {
        var fullUrl = gofirstConfig.domain + '/' + gofirstConfig.project + url;
        this._request(fullUrl,'GET', params,async,  true, callback);
    },
    //post方式发送请求
   /* sendPost: function (url, data, async, callback) {
        var fullUrl = gofirstConfig.domain + '/' + gofirstConfig.project + url;
        //var dataParams = JSON.stringify(data);
        //var dataParams = 'params=' + encodeURIComponent(JSON.stringify(data));
        var dataParams = "params=" + JSON.stringify(data) ;
        this._request(fullUrl,'POST',dataParams,async,false, callback);
    },*/
    sendPost: function (url, data, async, callback) {
    var fullUrl = gofirstConfig.domain + '/' + gofirstConfig.project + url;
    var dataParams = 'params=' + JSON.stringify(data);
    this._request(fullUrl, 'POST', async, dataParams, false, callback);
    },
    
    
    /**
     * 发起PUT请求
     * @param {Object} data put数据
     * @param {function} callback 结果回调函数
     * */
    sendPut: function (url, data, async, callback) {
        var fullUrl = gofirstConfig.domain + '/' + gofirstConfig.project + url;
        var dataParams = JSON.stringify(data);
        this._request(fullUrl, 'PUT',  dataParams,async,false, callback);
    },
    /**
     * 发起DELETE请求
     * @param {Object} params 查询参数
     * @param {function} callback 结果回调函数
     * */
    sendDelete: function (url, params, async, callback) {
        var fullUrl = gofirstConfig.domain + '/' + gofirstConfig.project + url;
        this._request(fullUrl, 'DELETE',  params,async,true, callback);
    },

    defaultErrorHandler:{
        onBusinessProcessError: function() {alert('businsss失败！！');  },
        onDBConnectError: function() { },
        onEntityWithoutIdError: function() { },
        onFieldValidateError: function() {  },
        onJsonConvertError: function() {},
        onJsonToJavaError: function() {  },
        onNoInterfaceError: function() {  },
        onNoServiceError: function() {  },
        onNotLogonError: function() { alert('未登录'); },
        onParameterCountError: function() {  },
        onParameterTypeError: function() {  },
        onPermissionDeniedError: function() {  },
        onSessionInvalidError: function() {  },
        onTimeoutError: function() { alert('请求超时, 请检查网络设置。'); },
        onVersionNotSupportError: function() {  },
        onUnhandledError: function(msg) { }
    },

    //fullUrl, 'GET', async, params, true, callback
    _request:function(url, method, data, async, processData, callback){
            $.ajax({
                url: url,
                type: method,
                data: data,
                processData: processData,
                cache: false,
                async: async,
                dataType: 'json',
                timeout: gofirstConfig.timeout || 30000,
             
                beforeSend: function (xhr) {
                },
                success: function(rawData, status, xhr) {
                    var errorCode = parseInt(xhr.getResponseHeader('error_code'));
                    if(errorCode === 200) {
                        // success
                       /* if(rawData && rawData != '') {
                            rawData = JSON.parse(rawData);
                        }*/
                        var errCode = 0;
                        if(rawData && rawData.errorCode != null && rawData.errorCode != undefined) {
                            errCode = rawData.errorCode;
                        }
                        var errMsg = 'succeed';
                        if(rawData && rawData.errorMsg != null && rawData.errorMsg != undefined) {
                            errMsg = rawData.errorMsg;
                        }

                        var totalCount = undefined;
                        if(rawData && typeof(rawData.totalCount) == 'number') {
                            totalCount = rawData.totalCount;
                        }
                        var pageCount = undefined;
                        if(rawData && typeof(rawData.pageCount) == 'number') {
                            pageCount = rawData.pageCount;
                        }

                        var resultData = undefined;
                        if(rawData && rawData.data) {
                            resultData = rawData.data;
                        }else if(rawData && rawData.data == 0) {
                            resultData = rawData.data;
                        }
                        if(typeof callback == 'function') {
                            callback(errCode, errMsg, resultData,totalCount,pageCount);
                        }
                    } else {
                        if(errorCode === 100) {
                            if(gofirstConfig && gofirstConfig.errorHandlers && gofirstConfig.errorHandlers.notLogonHandler) {
                                gofirstConfig.errorHandlers.notLogonHandler();
                            } else {
                            	baseRequest.defaultErrorHandler.onNotLogonError();
                            }
                        } else if(errorCode === 101) {
                            if(gofirstConfig && gofirstConfig.errorHandlers && gofirstConfig.errorHandlers.permissionDeniedHandler) {
                                gofirstConfig.errorHandlers.permissionDeniedHandler();
                            } else {
                            	baseRequest.defaultErrorHandler.onPermissionDeniedError();
                            }
                        } else if(errorCode === 102) {
                            if(gofirstConfig && gofirstConfig.errorHandlers && gofirstConfig.errorHandlers.versionNotSupportHandler) {
                                gofirstConfig.errorHandlers.versionNotSupportHandler();
                            } else {
                            	baseRequest.defaultErrorHandler.onVersionNotSupportError();
                            }
                        } else if(errorCode === 103) {
                            if(gofirstConfig && gofirstConfig.errorHandlers && gofirstConfig.errorHandlers.sessionInvalidHandler) {
                                gofirstConfig.errorHandlers.sessionInvalidHandler();
                            } else {
                            	baseRequest.defaultErrorHandler.onSessionInvalidError();
                            }
                        } else if(errorCode === 104) {
                            if(gofirstConfig && gofirstConfig.errorHandlers && gofirstConfig.errorHandlers.noServiceHandler) {
                                gofirstConfig.errorHandlers.noServiceHandler();
                            } else {
                            	baseRequest.defaultErrorHandler.onNoServiceError();
                            }
                        } else if(errorCode === 105) {
                            if(gofirstConfig && gofirstConfig.errorHandlers && gofirstConfig.errorHandlers.noInterfaceHandler) {
                                gofirstConfig.errorHandlers.noInterfaceHandler();
                            } else {
                               this.defaultErrorHandler.onNoInterfaceError();
                            }
                        } else if(errorCode === 106) {
                            if(gofirstConfig && gofirstConfig.errorHandlers && gofirstConfig.errorHandlers.jsonConvertErrorHandler) {
                                gofirstConfig.errorHandlers.jsonConvertErrorHandler();
                            } else {
                               this.defaultErrorHandler.onJsonConvertError();
                            }
                        } else if(errorCode === 107) {
                            if(gofirstConfig && gofirstConfig.errorHandlers && gofirstConfig.errorHandlers.parameterCountErrorHandler) {
                                gofirstConfig.errorHandlers.parameterCountErrorHandler();
                            } else {
                               this.defaultErrorHandler.onParameterCountError();
                            }
                        } else if(errorCode === 108) {
                            if(gofirstConfig && gofirstConfig.errorHandlers && gofirstConfig.errorHandlers.parameterTypeErrorHandler) {
                                gofirstConfig.errorHandlers.parameterTypeErrorHandler();
                            } else {
                               this.defaultErrorHandler.onParameterTypeError();
                            }
                        } else if(errorCode === 109) {
                            if(gofirstConfig && gofirstConfig.errorHandlers && gofirstConfig.errorHandlers.jsonToJavaErrorHandler) {
                                gofirstConfig.errorHandlers.jsonToJavaErrorHandler();
                            } else {
                               this.defaultErrorHandler.onJsonToJavaError();
                            }
                        } else if(errorCode === 110) {
                            if(gofirstConfig && gofirstConfig.errorHandlers && gofirstConfig.errorHandlers.businessProcessErrorHandler) {
                                gofirstConfig.errorHandlers.businessProcessErrorHandler();
                            } else {
                            	baseRequest.defaultErrorHandler.onBusinessProcessError();
                            }
                        } else if(errorCode === 111) {
                            if(gofirstConfig && gofirstConfig.errorHandlers && gofirstConfig.errorHandlers.entityWithoutIdHandler) {
                                gofirstConfig.errorHandlers.entityWithoutIdHandler();
                            } else {
                               this.defaultErrorHandler.onEntityWithoutIdError();
                            }
                        } else if(errorCode === 112) {
                            if(gofirstConfig && gofirstConfig.errorHandlers && gofirstConfig.errorHandlers.dbConnectErrorHandler) {
                                gofirstConfig.errorHandlers.dbConnectErrorHandler();
                            } else {
                               this.defaultErrorHandler.onDBConnectError();
                            }
                        } else if(errorCode == 113) {
                            if(gofirstConfig && gofirstConfig.errorHandlers && gofirstConfig.errorHandlers.fieldValidateErrorHandler) {
                                gofirstConfig.errorHandlers.fieldValidateErrorHandler();
                            } else {
                               this.defaultErrorHandler.onFieldValidateError();
                            }
                        } else {
                            if(gofirstConfig && gofirstConfig.errorHandlers && gofirstConfig.errorHandlers.unhandledErrorHandler) {
                                gofirstConfig.errorHandlers.unhandledErrorHandler('other error');
                            } else {
                               this.defaultErrorHandler.onUnhandledError('other error');
                            }
                        }
                        if(typeof callback == 'function') {
                            callback(-1, 'server error');
                        }
                    }
                },
                error: function(xhr, status, exception) {
                    if(status == 'timeout') {
                        if(gofirstConfig && gofirstConfig.errorHandlers && gofirstConfig.errorHandlers.timeoutHandler) {
                            gofirstConfig.errorHandlers.timeoutHandler();
                        } else {
                           this.defaultErrorHandler.onTimeoutError();
                        }
                    } else {
                        if(gofirstConfig && gofirstConfig.errorHandlers && gofirstConfig.errorHandlers.unhandledErrorHandler) {
                            gofirstConfig.errorHandlers.unhandledErrorHandler(exception.message || status);
                        } else {
                         this.defaultErrorHandler.onUnhandledError(exception.message || status);
                        }
                    }
                    if(typeof callback == 'function') {
                        callback(-1, 'network error');
                    }
                }

            });

        }

}




