<?php
require_once('connect_db.php');
	if(isset($_POST['action'])){
		if($_POST['action'] == 'activation'){
			$key = $_POST['key'];
			$date = $_POST['expiryDate'];
			
			$query = "SELECT `id`, `date`, `duration`, `key` FROM `expirydates` WHERE `key` = '$key' ";
			if($query_run = mysqli_query($mysqli,$query)){
				if(mysqli_num_rows($query_run)==1)
				{
					$iquery = "UPDATE `expirydates` SET `date`= '$date' WHERE `key` = '$key'";
					if($query_run = mysqli_query($mysqli, $iquery)){
						echo 'success';
					}else{
						echo 'failed';
					}
				}
			}else{
				echo 'wrongQuery';
			}	
		}else{
			echo 'failed';
		}
	}else{
		echo 'failed'; 
	}
?>