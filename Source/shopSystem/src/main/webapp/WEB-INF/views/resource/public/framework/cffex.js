;(function(root, factory) {
	if(typeof define === 'function') {
        // AMD. Register as an anonymous module.
        define('cffex', [], function(cffex) {
            // Also create a global in case some scripts
            // that are loaded still are looking for
            // a global even when an AMD loader is in use.
            return (root.cffex = factory(cffex));
        });
    } else if(typeof exports === 'object') {
        // Node. Does not work with strict CommonJS, but
        // only CommonJS-like enviroments that support module.exports,
        // like Node.
        module.exports = factory(require('cffex'));
    } else {
        // Browser globals (root is window)
        root.cffex = factory(root.cffex);
    }
}(this, function() {
	
	var cffex = {};


	/**
	* 设置依赖的jQuery(用于客户端使用模块化时)
	* */
	cffex.setJQuery = function(jquery) {
		cffex.internal.jquery = jquery;
	};

	/**
	* 远程权限访问对象
	* */
	cffex.permission = {};

	/**
	 * 登入
	 * @param {string} userName 用户名
	 * @param {string} password 密码
	 * @param {function} callback 回调函数
	 * */
	cffex.permission.logon = function(userName, password,verificationCode, callback) {
		var url = '/login';
		var params={
			"userName":userName,
			"password":password
		};
     	baseRequest.sendPost(url, params,false, callback);
	};
	
	/**
	 * 登出
	 * @param {function} callback 回调函数
	 * */
	cffex.permission.logout = function(callback) {
		var url = '/logout';
		cffex.internal.sendDelete(url, '', callback);
	};
	
	/**
	 * 获取用户权限
	 * @param {string} type 类型
	 * @param {function} callback 回调函数
	 * */
	cffex.permission.getPermissions = function(type, callback) {
		var url = '/user/profile';
		var params = {type: type};
		cffex.internal.sendGet(url, params, callback);
	};

	/**
	* 远程service访问对象
	* */
	cffex.service = {};

	/**
	 * 发起请求
	 * serviceRequest.request1('myService', 'myFunc', param1, param2, param3, ...);
	 * @param {string} serviceName 服务名
	 * @param {string} funcName 方法名
	 * @param {any...} 远程服务方法参数
	 * @param {function} callback 回调函数
	 * */
	cffex.service.request = function(serviceName, funcName) {
		var argsCount = arguments.length;
		if(argsCount < 2) {
			throw new error('call request with wrong params.');
		}
		var callback = undefined;
		var url = '/service/' + serviceName + '/' + funcName;
		var params = new Array();
		for (var i = 2; i < argsCount; i++) {
			var arg = arguments[i];
			if(i == argsCount - 1 && typeof(arg) == 'function') {
				callback = arg;
			} else {
				params.push(arg);
			}
		};
		var data = 'params=' + encodeURIComponent(JSON.stringify(params));
		cffex.internal.sendPost(url, data, callback);
	};

	/**
	 * 发起请求
	 * serviceRequest.request2('myService', 'myFunc', {param1:value1, param2:value2, param3:value3, ...});
	 * @param {string} serviceName 服务名
	 * @param {string} funcName 方法名
	 * @param {json} funcParams 远程服务方法Map形式参数
	 * @param {function} callback 回调函数
	 * */
	cffex.service.request2 = function(serviceName, funcName, funcParams, callback) {
		var argsCount = arguments.length;
		if(argsCount < 2) {
			throw new error('call request with wrong params.');
		}
		var url = '/service/' + serviceName + '/' + funcName;
		var params = '';
		if(typeof(funcParams) == 'object') {
			for (var key in funcParams) {
				if(params != '') {
					params += '&';
				}
				params += key + '=' + JSON.stringify(funcParams[key]);
			};
		}
		cffex.internal.sendPost(url, params, callback);
	};
    cffex.service.request3 = function(serviceName, funcName) {
        var argsCount = arguments.length;
        if(argsCount < 2) {
            throw new error('call request with wrong params.');
        }
        var callback = undefined;
        var url = '/service/' + serviceName + '/' + funcName;
        for (var i = 2; i < argsCount; i++) {
            var arg = arguments[i];
            if(i == argsCount - 1 && typeof(arg) == 'function') {
                callback = arg;
            } else {
                params.push(arg);
            }
        };
        cffex.internal.sendPost(url, callback);
    };
	/**
	* 远程repository访问对象
	* */
	cffex.repository = {};

	/**
	 * 排序规则：升序
	 */
	cffex.repository.SORT_TYPE_ASC = 1;
	/**
	 * 排序规则：降序
	 */
	cffex.repository.SORT_TYPE_DESC = 2;

	/**
	 * 创建数据
	 * @param {string} entityName 远程服务entity名
	 * @param {Object} data 要创建的数据
	 * @param {function} callback 回调函数
	 * */
	cffex.repository.create = function(entityName, data, callback) {
		var url = '/repository/' + entityName;
		var params = JSON.stringify(data);
		cffex.internal.sendPut(url, params, callback);
	};

	/**
	 * 删除数据
	 * @param {string} entityName 远程服务entity名
	 * @param {number} id 要删除的id
	 * @param {function} callback 回调函数S
	 * */
	cffex.repository.deleter = function(entityName, id, callback) {
		var url = '/repository/' + entityName + '/' + id;
		cffex.internal.sendDelete(url, null, callback);
	};
	
	/**
	 * 更新数据
	 * @param {string} entityName 远程服务entity名
	 * @param {number} id 要更新的id
	 * @param {Object} data 要更新的数据
	 * @param {boolean} isPartial 是否仅更新部分(默认：是)
	 * @param {function} callback 回调函数
	 * */
	cffex.repository.update = function(entityName, id, data, isPartial, callback) {
		if(typeof(isPartial) == 'function') {
			callback = isPartial;
			isPartial = true;
		}
		var url = '';
		if(isPartial) {
			url = '/repository/partUpdate/' + entityName + '/' + id;
		} else {
			url = '/repository/' + entityName + '/' + id;
		}
        var params = 'params=' + encodeURIComponent(JSON.stringify(data));
		cffex.internal.sendPost(url, params, callback);
	};

	/**
	 * 根据属性查询
	 * @param {string} entityName 远程服务entity名
	 * @param {string} funcName 远程服务方法名
	 * @param {json object} options 查询选项参数 {pageSize:10, pageIndex:0, sortFields:[{field:'field1', type:cffex.repository.SORT_TYPE_ASC},{...},...]}
	 * @param {any...} 远程服务方法参数
	 * @param {function} callback 回调函数
	 * */
	cffex.repository.query = function(entityName, funcName, options) {
		var argsCount = arguments.length;
		if(argsCount < 2) {
			throw new error('call query with wrong params.');
		}
		
		var url = '/repository/' + entityName + '/' + funcName;

		if(options && !isNaN(options.pageSize) && options.pageSize > 0) {
			var pageIndex = 0;
			if(!isNaN(options.pageIndex) && options.pageIndex > 0) {
				pageIndex = options.pageIndex;
			}
			url += '/' + pageIndex + '/' + options.pageSize;
		}
		
		var callback = undefined;
		var funcParams = new Array();
		for(var i = 3; i < argsCount; i++) {
			var arg = arguments[i];
			if(i == argsCount - 1 && typeof(arg) == 'function') {
				callback = arg;
			} else {
				funcParams.push(arg);
			}
		}
		var params = 'params=' + JSON.stringify(funcParams);

		if(options && options.sortFields && options.sortFields instanceof Array) {
			for(var i = 0; i < options.sortFields.length; i++) {
				var sortField = options.sortFields[i];
				if(sortField.field) {
					params += '&sort=' + sortField.field;
					if(sortField.type == cffex.repository.SORT_TYPE_DESC) {
						params += ',DESC';
					} else {
						params += ',ASC';
					}
				}
			};
		}
		
		cffex.internal.sendGet(url, params, callback);
	};

	/**
	 * 根据属性查询
	 * @param {string} entityName 远程服务entity名
	 * @param {string} funcName 远程服务方法名
	 * @param {json object} options 查询选项参数 {pageSize:10, pageIndex:0, sortFields:[{field:'field1', type:cffex.repository.SORT_TYPE_ASC},{...},...]}
	 * @param {json object} funcParams 远程方法Map形式参数
	 * @param {function} callback 回调函数
	 * */
	cffex.repository.query2 = function(entityName, funcName, options, funcParams, callback) {
		var argsCount = arguments.length;
		if(argsCount < 2) {
			throw new error('call query with wrong params.');
		}
		
		var url = '/repository/' + entityName + '/' + funcName;

		if(options && !isNaN(options.pageSize) && options.pageSize > 0) {
			var pageIndex = 0;
			if(!isNaN(options.pageIndex) && options.pageIndex > 0) {
				pageIndex = options.pageIndex;
			}
			url += '/' + pageIndex + '/' + options.pageSize;
		}

		var params = '';
		if(typeof(funcParams) == 'object') {
			for (var key in funcParams) {
				if(params != '') {
					params += '&';
				}
				params += key + '=' + JSON.stringify(funcParams[key]);
			};
		}

		if(options && options.sortFields && options.sortFields instanceof Array) {
			for(var i = 0; i < options.sortFields.length; i++) {
				var sortField = options.sortFields[i];
				if(sortField.field) {
					if(params != '') {
						params += '&';
					}
					params += 'sort=' + sortField.field;
					if(sortField.type == cffex.repository.SORT_TYPE_DESC) {
						params += ',DESC';
					} else {
						params += ',ASC';
					}
				}
			};
		}
		
		cffex.internal.sendGet(url, params, callback);
	};


    /**
     * 根据属性查询
     * @param {string} entityName 远程服务entity名
     * @param {string} funcName 远程服务方法名
     * @param {json object} options 查询选项参数 {pageSize:10, pageIndex:0, sortFields:[{field:'field1', type:cffex.repository.SORT_TYPE_ASC},{...},...]}
     * @param {any...} 参数
     * @param {function} callback 回调函数
     * */
    cffex.repository.query3 = function(entityName, funcName, options,param) {
        var argsCount = arguments.length;
        if(argsCount < 2) {
            throw new error('call query with wrong params.');
        }

        var url = '/repository/' + entityName + '/' + funcName;

        if(options && !isNaN(options.pageSize) && options.pageSize > 0) {
            var pageIndex = 0;
            if(!isNaN(options.pageIndex) && options.pageIndex > 0) {
                pageIndex = options.pageIndex;
            }
            url += '/' + pageIndex + '/' + options.pageSize;
        }

        var callback = undefined;
        //var funcParams = "";
        for(var i = 3; i < argsCount; i++) {
            var arg = arguments[i];
            if(i == argsCount - 1 && typeof(arg) == 'function') {
                callback = arg;
            } else {

            }
        }
        var params = 'params=' +param;

        if(options && options.sortFields && options.sortFields instanceof Array) {
            for(var i = 0; i < options.sortFields.length; i++) {
                var sortField = options.sortFields[i];
                if(sortField.field) {
                    params += '&sort=' + sortField.field;
                    if(sortField.type == cffex.repository.SORT_TYPE_DESC) {
                        params += ',DESC';
                    } else {
                        params += ',ASC';
                    }
                }
            };
        }

        cffex.internal.sendGet(url, params, callback);
    };


	/**
	* 文件处理对象
	* */
	cffex.file = {};

	/**
	 * 从远程下载文件
	 * @param {string} serviceName 远程服务名
	 * @param {string} funcName 远程服务方法名
	 * @param {any...} funcParams 远程方法参数
	 * @param {function} callback 回调函数
	 * */
	cffex.file.download = function(serviceName, funcName) {
		var argsCount = arguments.length;
		if(argsCount < 2) {
			throw new error('call download with wrong params.');
		}
		var callback = undefined;
		var url = cffex.internal.getFullUrl('/download/' + serviceName + '/' + funcName);
		var params = new Array();
		for (var i = 2; i < argsCount; i++) {
			var arg = arguments[i];
			if(i == argsCount - 1 && typeof(arg) == 'function') {
				callback = arg;
			} else {
				params.push(arg);
			}
		};
		url += '?params=' + JSON.stringify(params);
		window.open(url, '', 'location=no');
		if(typeof(callback) == 'function') {
			callback(0, 'download start succeed');
		}
	};

	/**
	 * 从远程下载文件
	 * @param {string} serviceName 远程服务名
	 * @param {string} funcName 远程服务方法名
	 * @param {json object} funcParams 远程方法Map形式参数
	 * @param {function} callback 回调函数
	 * */
	cffex.file.download2 = function(serviceName, funcName, funcParams, callback) {
		var argsCount = arguments.length;
		if(argsCount < 2) {
			throw new error('call download with wrong params.');
		}
		var url = cffex.internal.getFullUrl('/download/' + serviceName + '/' + funcName);
		var params = '';
		if(typeof(funcParams) == 'object') {
			for (var key in funcParams) {
				if(params != '') {
					params += '&';
				}
				params += key + '=' + JSON.stringify(funcParams[key]);
			};
		}
		if(params != '') {
			url += '?' + params;
		}
		window.open(url, '', 'location=no');
		if(typeof(callback) == 'function') {
			callback(0, 'download start succeed');
		}
	};

	/**
	 * 上传文件
	 * @param {string} serviceName 远程服务名
	 * @param {string} funcName 远程服务方法名
	 * @param {array} file控件id列表
	 * @param {function} 回调函数
	 * */
	cffex.file.upload = function(serviceName, funcName, fileElementIds, callback) {
		var url = cffex.internal.getFullUrl('/upload/' + serviceName + '/' + funcName);
		var jquery = cffex.internal.jquery || $;
		if(!jquery) {
			throw new error('cffex depends on jQuery, please import jQuery library or call cffex.setJQuery($).');
		}
		jquery.ajaxFileUpload({
			url: url,
			fileElementId: fileElementIds,
			secureuri: false,
			dataType: 'json',
			success: function(data, status) {
				if(typeof(callback) == 'function') {
					callback(0, 'upload succeed');
				}
			},
			error: function(data, status, error) {
				if(status == 'timeout') {
					if(cffexConfig && cffexConfig.errorHandlers && cffexConfig.errorHandlers.timeoutHandler) {
						cffexConfig.errorHandlers.timeoutHandler();
					} else {
						cffex.internal.defaultErrorHandler.onTimeoutError();
					}
				} else {
					if(cffexConfig && cffexConfig.errorHandlers && cffexConfig.errorHandlers.unhandledErrorHandler) {
						cffexConfig.errorHandlers.unhandledErrorHandler(error.message);
					} else {
						cffex.internal.defaultErrorHandler.onUnhandledError(error.message);
					}
				}
				if(typeof(callback) == 'function') {
					callback(-1, 'network error');
				}
			}
		});
	};

	/**
	* 内部对象，仅供内部调用
	* */
	cffex.internal = {};

	cffex.internal.jquery = null;

	// 默认错误处理
	cffex.internal.defaultErrorHandler = {
        /*		onBusinessProcessError: function() { alert('后台业务处理遇到未知错误'); },
         onDBConnectError: function() { alert('数据库无法获得连接'); },
         onEntityWithoutIdError: function() { alert('entity定义错误，没有主键Id'); },
         onFieldValidateError: function() { alert('字段验证发生异常'); },
         onJsonConvertError: function() { alert('返回对象无法做json对象转换'); },
         onJsonToJavaError: function() { alert('请求参数无法转换java对象'); },
         onNoInterfaceError: function() { alert('请求接口错误'); },
         onNoServiceError: function() { alert('请求无对应服务'); },
         onNotLogonError: function() { alert('未登录'); },
         onParameterCountError: function() { alert('请求参数个数错误'); },
         onParameterTypeError: function() { alert('请求参数类型错误'); },
         onPermissionDeniedError: function() { alert('无权限访问'); },
         onSessionInvalidError: function() { alert('会话失效'); },
         onTimeoutError: function() { alert('请求超时, 请检查网络设置。'); },
         onVersionNotSupportError: function() { alert('访问版本不支持'); },
         onUnhandledError: function(msg) { alert('未处理错误: ' + msg); }*/

        onBusinessProcessError: function() {  },
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
	};

	cffex.internal.request = function(url, method, data, processData, callback) {
		var jquery = cffex.internal.jquery || $;
		if(!jquery) {
			throw new error('cffex depends on jQuery, please import jQuery library or call cffex.setJQuery($).');
		}
		jquery.ajax({
			url: url,
			type: method,
			data: data,
			processData: processData,
			cache: false,
			async: true,
			dataType: 'text',
			timeout: cffexConfig.timeout || 30000,
			beforeSend: function(xhr) {},
			success: function(rawData, status, xhr) {
				var errorCode = parseInt(xhr.getResponseHeader('error_code'));
				if(errorCode === 200) {
					// success
					if(rawData && rawData != '') {
						rawData = JSON.parse(rawData);
					}
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
						callback(errCode, errMsg, resultData, totalCount, pageCount);
					}
				} else {
					if(errorCode === 100) {
						if(cffexConfig && cffexConfig.errorHandlers && cffexConfig.errorHandlers.notLogonHandler) {
							cffexConfig.errorHandlers.notLogonHandler();
						} else {
							cffex.internal.defaultErrorHandler.onNotLogonError();
						}
					} else if(errorCode === 101) {
						if(cffexConfig && cffexConfig.errorHandlers && cffexConfig.errorHandlers.permissionDeniedHandler) {
							cffexConfig.errorHandlers.permissionDeniedHandler();
						} else {
							cffex.internal.defaultErrorHandler.onPermissionDeniedError();
						}
					} else if(errorCode === 102) {
						if(cffexConfig && cffexConfig.errorHandlers && cffexConfig.errorHandlers.versionNotSupportHandler) {
							cffexConfig.errorHandlers.versionNotSupportHandler();
						} else {
							cffex.internal.defaultErrorHandler.onVersionNotSupportError();
						}
					} else if(errorCode === 103) {
						if(cffexConfig && cffexConfig.errorHandlers && cffexConfig.errorHandlers.sessionInvalidHandler) {
							cffexConfig.errorHandlers.sessionInvalidHandler();
						} else {
							cffex.internal.defaultErrorHandler.onSessionInvalidError();
						}
					} else if(errorCode === 104) {
						if(cffexConfig && cffexConfig.errorHandlers && cffexConfig.errorHandlers.noServiceHandler) {
							cffexConfig.errorHandlers.noServiceHandler();
						} else {
							cffex.internal.defaultErrorHandler.onNoServiceError();
						}
					} else if(errorCode === 105) {
						if(cffexConfig && cffexConfig.errorHandlers && cffexConfig.errorHandlers.noInterfaceHandler) {
							cffexConfig.errorHandlers.noInterfaceHandler();
						} else {
							cffex.internal.defaultErrorHandler.onNoInterfaceError();
						}
					} else if(errorCode === 106) {
						if(cffexConfig && cffexConfig.errorHandlers && cffexConfig.errorHandlers.jsonConvertErrorHandler) {
							cffexConfig.errorHandlers.jsonConvertErrorHandler();
						} else {
							cffex.internal.defaultErrorHandler.onJsonConvertError();
						}
					} else if(errorCode === 107) {
						if(cffexConfig && cffexConfig.errorHandlers && cffexConfig.errorHandlers.parameterCountErrorHandler) {
							cffexConfig.errorHandlers.parameterCountErrorHandler();
						} else {
							cffex.internal.defaultErrorHandler.onParameterCountError();
						}
					} else if(errorCode === 108) {
						if(cffexConfig && cffexConfig.errorHandlers && cffexConfig.errorHandlers.parameterTypeErrorHandler) {
							cffexConfig.errorHandlers.parameterTypeErrorHandler();
						} else {
							cffex.internal.defaultErrorHandler.onParameterTypeError();
						}
					} else if(errorCode === 109) {
						if(cffexConfig && cffexConfig.errorHandlers && cffexConfig.errorHandlers.jsonToJavaErrorHandler) {
							cffexConfig.errorHandlers.jsonToJavaErrorHandler();
						} else {
							cffex.internal.defaultErrorHandler.onJsonToJavaError();
						}
					} else if(errorCode === 110) {
						if(cffexConfig && cffexConfig.errorHandlers && cffexConfig.errorHandlers.businessProcessErrorHandler) {
							cffexConfig.errorHandlers.businessProcessErrorHandler();
						} else {
							cffex.internal.defaultErrorHandler.onBusinessProcessError();
						}
					} else if(errorCode === 111) {
						if(cffexConfig && cffexConfig.errorHandlers && cffexConfig.errorHandlers.entityWithoutIdHandler) {
							cffexConfig.errorHandlers.entityWithoutIdHandler();
						} else {
							cffex.internal.defaultErrorHandler.onEntityWithoutIdError();
						}
					} else if(errorCode === 112) {
						if(cffexConfig && cffexConfig.errorHandlers && cffexConfig.errorHandlers.dbConnectErrorHandler) {
							cffexConfig.errorHandlers.dbConnectErrorHandler();
						} else {
							cffex.internal.defaultErrorHandler.onDBConnectError();
						}
					} else if(errorCode == 113) {
						if(cffexConfig && cffexConfig.errorHandlers && cffexConfig.errorHandlers.fieldValidateErrorHandler) {
							cffexConfig.errorHandlers.fieldValidateErrorHandler();
						} else {
							cffex.internal.defaultErrorHandler.onFieldValidateError();
						}
					} else {
						if(cffexConfig && cffexConfig.errorHandlers && cffexConfig.errorHandlers.unhandledErrorHandler) {
							cffexConfig.errorHandlers.unhandledErrorHandler('other error');
						} else {
							cffex.internal.defaultErrorHandler.onUnhandledError('other error');
						}
					}
					if(typeof callback == 'function') {
						callback(-1, 'server error');
					}
				}
			},
			error: function(xhr, status, exception) {
				if(status == 'timeout') {
					if(cffexConfig && cffexConfig.errorHandlers && cffexConfig.errorHandlers.timeoutHandler) {
						cffexConfig.errorHandlers.timeoutHandler();
					} else {
						cffex.internal.defaultErrorHandler.onTimeoutError();
					}
				} else {
					if(cffexConfig && cffexConfig.errorHandlers && cffexConfig.errorHandlers.unhandledErrorHandler) {
						cffexConfig.errorHandlers.unhandledErrorHandler(exception.message || status);
					} else {
						cffex.internal.defaultErrorHandler.onUnhandledError(exception.message || status);
					}
				}
				if(typeof callback == 'function') {
					callback(-1, 'network error');
				}
			}
		});
	};

	cffex.internal.getFullUrl = function(url) {
		if(!cffexConfig || !cffexConfig.domain || !cffexConfig.project) {
			throw new error('no cffexConfig found.');
		}
		return cffexConfig.domain + '/' + cffexConfig.project + url;
	}

	/**
	 * 发起GET请求
	 * @param {string} params 查询参数
	 * */
	cffex.internal.sendGet = function(url, params, callback) {
		var fullUrl = cffex.internal.getFullUrl(url);
		cffex.internal.request(fullUrl, 'GET', params, true, callback);
	};
	
	/**
	 * 发起POST请求
	 * @param {string} data 要post的数据
	 * */
	cffex.internal.sendPost = function(url, data, callback) {
		var fullUrl = cffex.internal.getFullUrl(url);
		cffex.internal.request(fullUrl, 'POST', data, false, callback);
	};
	
	/**
	 * 发起PUT请求
	 * @param {string} data 要put的数据
	 * */
	cffex.internal.sendPut = function(url, params, callback) {
		var fullUrl = cffex.internal.getFullUrl(url);
		cffex.internal.request(fullUrl, 'PUT', params, false, callback);
	};
	
	/**
	 * 发起DELETE请求
	 * @param {string} params 查询参数
	 * */
	cffex.internal.sendDelete = function(url, params, callback) {
		var fullUrl = cffex.internal.getFullUrl(url);
		cffex.internal.request(fullUrl, 'DELETE', params, true, callback);
	};

	return cffex;
}));