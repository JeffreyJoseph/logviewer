	//Main Javascript file
	function getXmlHttpRequestObject() {
		if (window.XMLHttpRequest) {
			return new XMLHttpRequest(); //To support the browsers IE7+, Firefox, Chrome, Opera, Safari
		} else if(window.ActiveXObject) {
			return new ActiveXObject("Microsoft.XMLHTTP"); // For the browsers IE6, IE5
		} else {
			alert("Error due to old verion of browser upgrade your browser");
		}
	}
	
	var rcvReq = getXmlHttpRequestObject();
	fetchStages();
	
	/* Global variables */
	var g_set_time_out = 0;
	var g_ret_val = null;
	var g_server = null;
	var g_log_file = null;
	var g_lines_count = "1000";
	var g_globalObject = null;
	var g_total_pages = 0;
	
	/* Reads the value of Refresh Logs checkbox */
	function readAutoRefresh() {
		var check = document.getElementById("refresh");
		if (check.checked == true)
			return true;
		else
			return false;
	}
	
	/* Reads the value of Logs Archive check box */
	function readLogsArchiveCheckBox() {
		var check = document.getElementById("prevlog");
		if (check.checked == true)
			return true;
		else
			return false;
	}
	
	/* FUNCTION A - Fetches the stages and managed servers*/
	function fetchStages() {
		
		if (rcvReq.readyState == 4 || rcvReq.readyState == 0) {
			rcvReq.open("GET", 'FetchEnvironments', true);
			rcvReq.onreadystatechange = handleStageContent; 
			rcvReq.send(null);
		} 
	}
	
	function handleStageContent() {
		if (rcvReq.readyState == 4) {
			document.getElementById("stagelist").innerHTML = rcvReq.responseText;
		}
	}
	/* FUNCTION A ENDS */
	
	/* FUNCTION B - Fetches the log file list from the selected stage */
	function fetchLoglist(server) {
		var prevday = readLogsArchiveCheckBox();
		
		var obj = g_globalObject.getSelectedDay();
		var month = (obj.month<10)?"0"+obj.month:obj.month;
		var day = (obj.day<10)?"0"+obj.day:obj.day;
		var prevdate =  obj.year + "-" + month + "-" + day;
		
		if(server!=null){
			g_server = server;
			
			if (rcvReq.readyState == 4 || rcvReq.readyState == 0 || rcvReq.readyState == 1) {
				rcvReq.open("GET", 'FetchLogfileList?server='+server+"&prevday="+prevday+"&prevdate="+prevdate, true);			
				rcvReq.onreadystatechange = handleLoglistContent; 
				rcvReq.send(null);
			} 
		}
		else if(g_server!=null){
			
			if (rcvReq.readyState == 4 || rcvReq.readyState == 0 || rcvReq.readyState == 1) {
				rcvReq.open("GET", 'FetchLogfileList?server='+g_server+"&prevday="+prevday+"&prevdate="+prevdate, true);			
				rcvReq.onreadystatechange = handleLoglistContent; 
				rcvReq.send(null);
			}
		}			
	}
	
	function handleLoglistContent() {
		if (rcvReq.readyState == 4) {
			document.getElementById("stagelist").innerHTML = rcvReq.responseText;
		}
	}
	/* FUNCTION B ENDS */
	
	/* FUNCTION C - Fetches the log file content from the selected file */
	function fetchLogfileContent(file){
		document.getElementById("pageno").value = "";
		
		if(file!=null)
			preloader();
		if(g_set_time_out==1){
			clearTimeout(g_ret_val);
			g_set_time_out = 1;
			inner_fetchLogfileContent(file);
		}
		else{
			g_set_time_out = 1;
			inner_fetchLogfileContent(file);
		}
	}
	
	function inner_fetchLogfileContent(file){
		
		
		if(file!=null){
			g_log_file = file;
			
			if (rcvReq.readyState == 4 || rcvReq.readyState == 0 || rcvReq.readyState == 1) {
				console.log("Inside inner_fetchLogfileContent case 1");
				rcvReq.open("GET", 'FetchLogfileContent?file='+file, true);			
				rcvReq.onreadystatechange = handleLogContent; 
				rcvReq.send(null);
			}
			var loop = function(){inner_fetchLogfileContent(file);};
			if(readAutoRefresh()){
				g_ret_val = setTimeout(loop, 4000);
			}
		}
		else if(g_log_file!=null){
			if (rcvReq.readyState == 4 || rcvReq.readyState == 0 || rcvReq.readyState == 1) {
				rcvReq.open("GET", 'FetchLogfileContent?file='+g_log_file, true);			
				rcvReq.onreadystatechange = handleLogContent; 
				rcvReq.send(null);
			}
			var loop = function(){inner_fetchLogfileContent(file);};
			if(readAutoRefresh()){
				g_ret_val = setTimeout(loop, 4000);
			}
		}		
	}
	
	function handleLogContent() {
		if (rcvReq.readyState == 4) {
			var pages = rcvReq.responseText.toString();
			g_total_pages = pages.substring(pages.indexOf("<!--pages:")+10,pages.indexOf(";-->"));
			document.getElementById("logcontent").innerHTML = rcvReq.responseText;
			document.getElementById("totalpages").value = g_total_pages;
		}
	}
	/* FUNCTION C ENDS */
	
	/* FUNCTION D - Searches the log file content for the specified keyword */
	function searchLogContent(){
		var text = document.getElementById("searchtext").value;
		var pageNumber = document.getElementById("pageno").value;
		
		if(g_log_file==null)
			alert("Please select the log file and then click Search");
		else if(text==null || text=="")
			alert("Please enter Search Text and then click Search");
		else{
			if(pageNumber=="" || pageNumber==null || isNaN(pageNumber)==true)
				pageNumber=-1;
			document.getElementById("refresh").checked = false;
			//document.getElementById("refresh").disabled = true;
			if(g_set_time_out==1){
				g_set_time_out = 0;
				clearTimeout(g_ret_val);
			}
			inner_searchLogContent(text, pageNumber);
		}
	}
	
	function inner_searchLogContent(text, pageNumber){
			
		if (rcvReq.readyState == 4 || rcvReq.readyState == 0 || rcvReq.readyState == 1) {
			rcvReq.open("GET", 'SearchLogfileContent?file='+g_log_file+"&text="+text+"&pageno="+pageNumber, true);			
			rcvReq.onreadystatechange = handleSearchContent; 
			rcvReq.send(null);
		}
	}
	
	function handleSearchContent() {
		if (rcvReq.readyState == 4) {
			var pages = rcvReq.responseText.toString();
			g_total_pages = pages.substring(pages.indexOf("<!--pages:")+10,pages.indexOf(";-->"));
			document.getElementById("logcontent").innerHTML = rcvReq.responseText;
			document.getElementById("totalpages").value = g_total_pages;
		}
	}
	/* FUNCTION D ENDS */
	
	/* FUNCTION E - Fetches the specified log file page from the selected log file */
	function fetchLogContentPages(){	
		var pageNumber = document.getElementById("pageno").value;
		
		if (g_log_file == null)
			alert("Please select the JVM/Configuration and log file to view");
		else if (pageNumber==null || pageNumber=="")
			alert("Please enter the page number");
		else if (isNaN(pageNumber)==true)
			alert("Please enter only numbers");
		else if (parseInt(pageNumber,10) >= parseInt(g_total_pages,10))
			alert("Page Number entered should be less than the Total Pages");
		else{	
			//g_lines_count = linesValue;
			
			//document.getElementById("refresh").disabled = true;
			if(g_set_time_out==1){
				document.getElementById("refresh").checked = false;
				g_set_time_out = 0;
				clearTimeout(g_ret_val);
			}
			
			inner_fetchLogContentPages(pageNumber); 
		}
	}
	
	function inner_fetchLogContentPages(pageNumber){
		if(g_log_file!=null){
			if (rcvReq.readyState == 4 || rcvReq.readyState == 0 || rcvReq.readyState == 1) {
				rcvReq.open("GET", 'FetchLogfileContentPages?file='+g_log_file+"&pageno="+pageNumber+"&total="+g_total_pages, true);			
				rcvReq.onreadystatechange = handlePagesContent; 
				rcvReq.send(null);
			}
		}
	}
	
	function handlePagesContent() {
		if (rcvReq.readyState == 4) {
			document.getElementById("logcontent").innerHTML = rcvReq.responseText;
		}
	}
    /* FUNCTION E ENDS */
	
	/* Attached to Enter Page No text field */
	function pageNoEntered(e){
		if(e && e.keyCode == 13)
			fetchLogContentPages();
	}
	
	/* Attached to Search text field */
	function searchKeyEntered(e){
		if(e && e.keyCode == 13)
			searchLogContent();
	}
	
	/* Displays loader before log file is displayed */
	function preloader(){
        document.getElementById("logcontent").innerHTML = 
        	"<img src=\"loader.gif\" alt=\"Loading...\ style=\"top:500px; left:500px;\" >";
    }

	/* Actions performed when Refresh Logs option is changed */
	function autoRefreshClick(){
		if(readAutoRefresh()){
			document.getElementById("prevlog").checked = false;
			document.getElementById("datefield").value = "";
			document.getElementById("datefield").disabled = true;
			document.getElementById("submit").disabled = true;
			fetchLogfileContent(null);
		}
		else if(!readAutoRefresh()){
			if(g_set_time_out==1){
				g_set_time_out = 0;
				clearTimeout(g_ret_val);
			}
		}
	}
	
	/* Actions performed when Logs Archive option is changed */
	function logsArchiveCheckBoxClick(){
		if(readLogsArchiveCheckBox()){
			document.getElementById("refresh").checked = false;
			//document.getElementById("refresh").disabled = true;
			document.getElementById("datefield").disabled = false;
			document.getElementById("submit").disabled = false;
		}
		else if(!readLogsArchiveCheckBox()){
			//fetchLoglist(null);
			document.getElementById("refresh").disabled = false;
			document.getElementById("refresh").checked = true;
			document.getElementById("datefield").value = "";
			document.getElementById("datefield").disabled = true;
			document.getElementById("submit").disabled = true;
			fetchLoglist(null);
		}
	}
	
	/* Function which displays the calendar */
	window.onload = function(){
		g_globalObject = new JsDatePick({
			useMode:2,
			target:"datefield",
			dateFormat:"%d-%M-%Y",
			limitToToday:true
			/*selectedDate:{				This is an example of what the full configuration offers.
				day:5,						For full documentation about these settings please see the full version of the code.
				month:9,
				year:2006
			},
			yearsRange:[1978,2020],
			limitToToday:false,
			cellColorScheme:"beige",
			dateFormat:"%m-%d-%Y",
			imgPath:"img/",
			weekStartDay:1*/
		});
		
		/*g_globalObject.setOnSelectedDelegate(function(){
			var obj = g_globalObject.getSelectedDay();
			alert("a date was just selected and the date is : " + obj.day + "/" + obj.month + "/" + obj.year);
			document.getElementById("datefield").innerHTML = obj.day + "/" + obj.month + "/" + obj.year;
		});*/
	};