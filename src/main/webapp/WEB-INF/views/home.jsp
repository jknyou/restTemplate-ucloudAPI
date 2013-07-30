<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page session="false"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<meta name="description" content="" />
		<meta name="author" content="" />
		<link href="http://netdna.bootstrapcdn.com/bootstrap/3.0.0-rc1/css/bootstrap.min.css" rel="stylesheet">
		
		<style type="text/css">
			body {
				padding-top: 40px;
				padding-bottom: 40px;
				background-color: #f5f5f5;
			}
			
			.form-signin {
				max-width: 300px;
				padding: 19px 29px 29px;
				margin: 0 auto 20px;
				background-color: #fff;
				border: 1px solid #e5e5e5;
				-webkit-border-radius: 5px;
				-moz-border-radius: 5px;
				border-radius: 5px;
				-webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
				-moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
				box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
			}
			
			.form-signin .form-signin-heading,.form-signin .checkbox {
				margin-bottom: 10px;
			}
			
			.form-signin input[type="text"],.form-signin input[type="password"] {
				font-size: 16px;
				height: auto;
				margin-bottom: 15px;
				padding: 7px 9px;
			}
		</style>
		
		<script src="http://code.jquery.com/jquery-2.0.3.min.js"></script>
		<script src="http://netdna.bootstrapcdn.com/bootstrap/3.0.0-rc1/js/bootstrap.min.js"></script>
	</head>
	<body>
		<div class="navbar navbar-inverse navbar-fixed-top bs-docs-nav">
			<div class="container">
				<a class="navbar-brand" href=""><spring:eval expression="@defaultProperties.getProperty(\'project.name\')" /></a>
			</div>
		</div>
		<div class="container" style="padding-top: 100px">
			<div class="row">
				<div class="col-lg-3">
					<div class="bs-sidebar">
						<ul class="nav bs-sidenav">
							<li><a id="pdListBt">Product List</a></li>
							<li><a id="svListBt">Server List</a></li>
							<li><a id="ipListBt">IP List</a></li>
							<li><a id="reqBt">Request</a></li>
						</ul>
					</div>
				</div>
				<div class="col-lg-9">
					<button id="addBt" type="button" class="btn btn-primary">ADD</button>
					<div id="totalCnt"></div>
					<div id="listSection"></div>
				</div>
			</div>
		</div>
		<!-- Modal -->
		<div id="svModal" class="modal">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
						<h3>Create Server</h3>
					</div>
					<div class="modal-body">
						<form>
							<fieldset>
							<div class="form-group">
								<label for="serviceofferingid">serviceofferingid</label>
								<input type="text" class="form-control" id="serviceofferingid" value="267734e0-9f7f-4dbe-aee7-f2bfcc9662d8">
							</div>
							<div class="form-group">
								<label for="templateid">templateid</label>
								<input type="text" class="form-control" id="templateid" value="c2e4c0f2-2bc7-444c-a280-59f5b42dbf11">
							</div>
							<div class="form-group">
								<label for="diskofferingid">diskofferingid</label>
								<input type="text" class="form-control" id="diskofferingid" value="87c0a6f6-c684-4fbe-a393-d8412bcf788d">
							</div>
							<div class="form-group">
								<label for="zoneid">zoneid</label>
								<input type="text" class="form-control" id="zoneid" value="eceb5d65-6571-4696-875f-5a17949f3317">
							</div>
							<div class="form-group">
								<label for="usageplantype">usageplantype</label>
								<input type="text" class="form-control" id="usageplantype" value="monthly">
							</div>
							<div class="form-group">
								<label for="runsysprep">runsysprep</label>
								<input type="text" class="form-control" id="runsysprep" value="false">
							</div>
							<div class="form-group">
								<label for="displayname">displayname</label>
								<input type="text" class="form-control" id="displayname" value="testdisplayname">
							</div>
							</fieldset>
						</form>
					</div>
					<div class="modal-footer">
						<button class="btn btn-large btn-primary" data-dismiss="modal" aria-hidden="true" id="createBt">Create</button>
					</div>
				</div>
			</div>
		</div>
		<!-- Modal -->
		<div id="ipModal" class="modal">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
						<h3>Create IP</h3>
					</div>
					<div class="modal-body">
						<form>
							<fieldset>
							<div class="form-group">
								<label for="zoneid">zoneid</label>
								<input type="text" class="form-control" id="zoneid" value="eceb5d65-6571-4696-875f-5a17949f3317">
							</div>
							<div class="form-group">
								<label for="usageplantype">usageplantype</label>
								<input type="text" class="form-control" id="usageplantype" value="hourly">
							</div>
							</fieldset>
						</form>
					</div>
					<div class="modal-footer">
						<button class="btn btn-large btn-primary" data-dismiss="modal" aria-hidden="true" id="createIpBt">Create</button>
					</div>
				</div>
			</div>
		</div>
		
		<script type="text/javascript">
			$(document).ready(function() {
				var addFlag = false;
				
				var getServerList = function(){
					$.getJSON('server', function(json){
						var data = JSON.parse(json.data);
						var page = '';
						page += '<table class="table table-striped">';
						page += '<tr>';
						page += '<th>displayname</th><th>serviceofferingname</th><th>templatedisplaytext</th><th>templatename</th><th>state</th><th>zonename</th>';
						page += '</tr>';
						$.each(data.listvirtualmachinesresponse.virtualmachine, function(k, v){
							page+='<tr>';
							page+='<td>'+ v.displayname +'</td>';
							page+='<td>'+ v.serviceofferingname +'</td>';
							page+='<td>'+ v.templatedisplaytext +'</td>';
							page+='<td>'+ v.templatename +'</td>';
							page+='<td>'+ v.state +'</td>';
							page+='<td>'+ v.zonename +'</td>';
							page+='<td><a class="start" vmId="'+ v.id +'">Start</a></td>';
							page+='<td><a class="stop" vmId="'+ v.id +'">Stop</a></td>';
							page+='<td><a class="remove" vmId="'+ v.id +'">del</a></td>';
							page+='</tr>';
							
						});
						page += '</table>';
						
						$('#totalCnt').html('<h3> count : '+data.listvirtualmachinesresponse.count+'</h3>');
						$('#listSection').html(page);
						addFlag = false;
					});
				};
				
				$('#svListBt').click(function(){
					getServerList();
				});
				
				$('#ipListBt').click(function(){
					$.getJSON('ip', function(json){
						var data = JSON.parse(json.data);
						var page = '';
						page += '<table class="table table-striped">';
						page += '<tr>';
						page += '<th>id</th><th>ipaddress</th><th>allocated</th><th>zonename</th>';
						page += '</tr>';
						$.each(data.listpublicipaddressesresponse.publicipaddress, function(k, v){
							page+='<tr>';
							page+='<td>'+ v.id +'</td>';
							page+='<td>'+ v.ipaddress +'</td>';
							page+='<td>'+ v.allocated +'</td>';
							page+='<td>'+ v.zonename +'</td>';
							page+='<td><a class="ipremove" ipId="'+ v.id +'">del</a></td>';
							page+='</tr>';
							
						});
						page += '</table>';
						
						$('#totalCnt').html('<h3> count : '+data.listpublicipaddressesresponse.count+'</h3>');
						$('#listSection').html(page);
						addFlag = true;
					});
				});
				
				$('#pdListBt').click(function(){
					$.getJSON('product', function(json){
						var data = JSON.parse(json.data);
						var page = '';
						page += '<table class="table table-striped">';
						page += '<tr>';
						page += '<th>diskofferingdesc</th><th>serviceofferingdesc</th><th>templatedesc</th><th>zonedesc</th>';
						page += '</tr>';
						$.each(data.listavailableproducttypesresponse.producttypes, function(k, v){
							page+='<tr>';
							page+='<td>'+ v.diskofferingdesc +'</td>';
							page+='<td>'+ v.serviceofferingdesc +'</td>';
							page+='<td>'+ v.templatedesc +'</td>';
							page+='<td>'+ v.zonedesc +'</td>';
							page+='</tr>';
							
						});
						page += '</table>';
						
						$('#totalCnt').html('<h3> count : '+data.listavailableproducttypesresponse.count+'</h3>');
						$('#listSection').html(page);
					});
				});
				
				$('#addBt').click(function(){
					if(addFlag){
						$('#ipModal').modal('show');
						
					} else {
						$('#svModal').modal('show');
						
					}
				});
				
				
				$('#listSection').click(function(event){
					var target = $(event.target);
					if(target.hasClass('stop')){
						var id = target.attr('vmId');
						var paramList = [];
						paramList.push('command=stopVirtualMachine');
						paramList.push('id=' + id);
						postAjax(paramList);
					} else if(target.hasClass('remove')){
						var id = target.attr('vmId');
						var paramList = [];
						paramList.push('command=destroyVirtualMachine');
						paramList.push('id=' + id);
						postAjax(paramList);
					} else if(target.hasClass('start')){
						var id = target.attr('vmId');
						var paramList = [];
						paramList.push('command=startVirtualMachine');
						paramList.push('id=' + id);
						postAjax(paramList);
					} else if(target.hasClass('ipremove')){
						var id = target.attr('ipId');
						var paramList = [];
						paramList.push('command=disassociateIpAddress');
						paramList.push('id=' + id);
						postAjax(paramList);
					}
				});
				
				
				$('#createBt').click(function(event) {
					var paramList = [];
					paramList.push('command=deployVirtualMachine');
					paramList.push('serviceofferingid=' + $('#serviceofferingid').val());
					paramList.push('templateid=' + $('#templateid').val());
					paramList.push('diskofferingid=' + $('#diskofferingid').val());
					paramList.push('zoneid=' + $('#zoneid').val());
					paramList.push('usageplantype=' + $('#usageplantype').val());
					paramList.push('runsysprep=' + $('#runsysprep').val());
					paramList.push('displayname=' + $('#displayname').val());
					
					postAjax(paramList);
				});
				
				$('#createIpBt').click(function(event) {
					var paramList = [];
					paramList.push('command=associateIpAddress');
					paramList.push('zoneid=' + $('#zoneid').val());
					paramList.push('usageplantype=' + $('#usageplantype').val());
					
					postAjax(paramList);
				});
				
				var postAjax = function(paramList){
					$.ajax({
						type : "POST",
						url: "send",
						data : JSON.stringify(paramList),
						contentType: "application/json",
						dataType : "json",
						}).done(function() {
							getServerList();
						});
				};
				getServerList();
			});
		</script>
	</body>
</html>