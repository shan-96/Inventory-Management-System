<?php
	require('connect_db.php');
	$response= '';
	if($_POST['action'] == 'expiryDate'){
		$id = $_POST['id'];
		$query = "SELECT `date` FROM `expirydates` WHERE `id` = $id";
		if($query_run=mysqli_query($mysqli,$query)){
			while($row = mysqli_fetch_assoc($query_run)) {
                $response.=$row['date'];
			}	
		    echo $response;
		}else{
			echo 'wrongQuery';
		}	
		
	}else{
		echo 'wrongAction';
	}
?>