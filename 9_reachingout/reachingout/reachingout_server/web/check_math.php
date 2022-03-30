<?php
	if (array_key_exists('answer', $_POST)) {
		$answer= $_POST['answer'];
		if ($answer == "4") {
			echo "Here's your reward: FLAG{non_nobis_solum_nati_sumus}";;
		} else {
			echo "Wrong math!";
		}
	} else {
		echo "Answer not found.";
	}
?>
