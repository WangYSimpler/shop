//将动态生成列表的部分封装成函数，可以重复调用，也方便统一修改
function creatTable(parent, headers, datas) {

    var table = document.createElement("table");
    table.id = "tb";
    parent.appendChild(table);

    var thead = document.createElement("thead");
    table.appendChild(thead);

    var tr = document.createElement("tr");
    thead.appendChild(tr);

    for(var k in headers[0]){
        var th = document.createElement("th");

        th.innerHTML = headers[0][k];
        tr.appendChild(th);

    }

    var tbody = document.createElement("tbody");
    table.appendChild(tbody);

    for (var i = 0; i < datas.length; i++) {
        var tr = document.createElement("tr");
        tbody.appendChild(tr);

        for (var k in  headers[0]) {

            if(k != 'delete' && k != 'add'){
                var td = document.createElement("td");
                td.innerHTML = datas[i][k];
                tr.appendChild(td);
            }
        }
        var tdAdd = document.createElement("td");
        tdAdd.innerHTML = "<a href='javascript:'>添加</a>";
        tr.appendChild(tdAdd);
        
        var td = document.createElement("td");
        td.innerHTML = "<a href='javascript:'>删除</a>";
        tr.appendChild(td);

        td.children[0].onclick = function () {
            var lines = tbody.children.length;
            if (lines <= 1) {
                alert("最后一条！请留一点数据吧！");
                return;
            }
            var tip = confirm("确认删除？");
            if (tip) {
                tbody.removeChild(this.parentNode.parentNode);
            }

        }
    }
}

/**
createTable(toid, jsondata, check, edit, del)：用于动态创建table，第0行为表头，数据里必须包含表头和数据的id
@toid：创建table到id为toid的节点下
@jsondata：用于创建table的json格式的数据（须在jsondata里包含表头标题）
@check：是否创建查看按钮
@edit：是否创建编辑按钮
@del：是否创建删除按钮
*/
function createTable(toid, jsondata, check, edit, del) {
var table = document.createElement("table");
var tr, td;
for (i in jsondata) {
	tr = document.createElement("tr"); //创建tr
	//________________创建表头________________________________________
	if (i == 0) {
		for (j in jsondata[i]) { //根据数据在tr内创建td
			td = document.createElement("td");
			td.appendChild(document.createTextNode(jsondata[i][j]));
			if (j == "id") { //创建隐藏的td来存放id
				td.style.display = "none";
			}
			td.style.background = "#C1DAD7"; //设置表头颜色
			tr.appendChild(td);
		}
		if (check == true) { //创建查看按钮
			td = document.createElement("td");
			td.appendChild(document.createTextNode("查看"));
			td.style.background = "#C1DAD7"; //设置表头颜色
			tr.appendChild(td);
		}
		if (edit == true) { //创建编辑按钮
			td = document.createElement("td");
			td.appendChild(document.createTextNode("编辑"));
			td.style.background = "#C1DAD7"; //设置表头颜色
			tr.appendChild(td);
		}
		if (del == true) { //创建删除按钮
			td = document.createElement("td");
			td.appendChild(document.createTextNode("删除"));
			td.style.background = "#C1DAD7"; //设置表头颜色
			tr.appendChild(td);
		}
	}
	//________________创建数据行________________________________________
	else {
		for (j in jsondata[i]) { //根据数据在tr内创建td
			td = document.createElement("td");
			td.appendChild(document.createTextNode(jsondata[i][j]));
			if (j == "id") { //创建隐藏的td来存放id
				td.style.display = "none";
			}
			tr.appendChild(td);
		}
		if (check == true) { //创建查看按钮
			td = document.createElement("td");
			var btnCheck = document.createElement("button");
			btnCheck.appendChild(document.createTextNode("查看"));
			td.appendChild(btnCheck);
			tr.appendChild(td);
		}
		if (edit == true) { //创建编辑按钮
			td = document.createElement("td");
			var btnEdit = document.createElement("button");
			btnEdit.appendChild(document.createTextNode("编辑"));
			td.appendChild(btnEdit);
			tr.appendChild(td);
		}
		if (del == true) { //创建删除按钮
			td = document.createElement("td");
			var btnDel = document.createElement("button");
			btnDel.appendChild(document.createTextNode("删除"));
			td.appendChild(btnDel);
			tr.appendChild(td);
		}
	}
	table.appendChild(tr);
}
document.getElementById(toid).appendChild(table);
}