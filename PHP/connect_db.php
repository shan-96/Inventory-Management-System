<?php
	$mysql_host = 'localhost';
	$mysql_user = 'root';
	$mysql_pass = '';
	$mysql_db = 'mysoftware';
	
	if(!@($mysqli=new mysqli($mysql_host, $mysql_user, $mysql_pass,$mysql_db)) || $mysqli->connect_errno){
		echo 'Database connection error.';
		die();
	}

?>