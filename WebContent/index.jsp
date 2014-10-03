<!DOCTYPE jsp PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="stylesheet.css" />
<link rel="stylesheet" type="text/css" media="all" href="jsDatePick_ltr.min.css" />
<script type="text/javascript" src="RetrievalFunctions.js"></script>
<script type="text/javascript" src="jsDatePick.min.1.3.js"></script>

</head>
<body>


<!-- LEFT PANE FOR STAGES -->
<div class="leftpane" id="leftpane">

	<div class="leftpane_logo" id="leftpane_logo"><img src="asu.png" alt="ASU"/><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
	<div class="leftpane_refresh" id="leftpane_refresh">
		<input type="checkbox" class="refresh" id="refresh" checked="checked" onclick="autoRefreshClick()"/>Refresh Logs<br/>
	</div>
	<div class="prevday" id="prevday">
		<input type="checkbox" class="prevlog" id="prevlog" onclick="logsArchiveCheckBoxClick()"/>Logs Archive<br/>
		<input type="text" size="16" class="datefield" id="datefield" onselect="pop()" placeholder="Click here to select date" disabled="disabled"/><br/>
		<input type="button" class="submit" id="submit" value="Submit" onclick="fetchLoglist()" disabled="disabled"/>
	</div>
		
	<div class="stagelistdiv" id="stagelistdiv">
		<p class="stagelist" id="stagelist"></p>
	</div>
	
</div>
<!-- LEFT PANE FOR STAGES ENDS -->

<!-- INSIDE RIGHT PANE - HEADER -->
<div class="header" id="header">
	<h2>Log Viewer</h2><br/>
</div>

<!-- INSIDE RIGHT PANE - MENU -->
<div class="nooflines" id="nooflines">
	Total Pages
	<input type="text" size="22" class="totalpages" id="totalpages" placeholder="Total" readonly/>
	Enter Page No
	<input type="text" size="22" class="pageno" id="pageno" placeholder="Page No" onkeypress="pageNoEntered(event)"/>
	<input type="button" class="linesbutton" id="linesbutton" value="Fetch Logs" onclick="fetchLogContentPages()"/>
	
	<input type="button" class="searchbutton" id="searchbutton" value="Search" onclick="searchLogContent()" style="float:right"/>
	<input type="text" size="22" class="searchtext" id="searchtext" placeholder="Enter Search Text here" style="float:right" onkeypress="searchKeyEntered(event)"/>
</div>

<!-- INSIDE RIGHT PANE - LOG CONTENT -->
<div class="logcontent" id="logcontent">
	<h4>Please select the Web Logic server and Log file to view</h4>
</div>

</body>
</html>