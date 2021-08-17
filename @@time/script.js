//Firebase with Web: https://firebase.google.com/docs/database/web/read-and-write

$( document ).ready(function() {
	
	//Scroll to top of page by default
	$('html, body').animate({
		scrollTop: $("body").offset().top
	}, 300);
	
	
	//Load all the activities list from Firebase
	console.log("Fetching all the activities list from Firebase....");
	database.ref('/activities')
    .once('value', (snapshot) =>{
		let data = snapshot.val();
		Object.keys(data).map(key =>{ 
			$(".activities").append("<div style='background-color:"+data[key].color+"'>"+key+"</div>");
		})
    })
	
	
    $("#start-here, #check-more").click(function() {
		$('html, body').animate({
			scrollTop: $(".section-activity-list").offset().top
		}, 300);
	});
	
	//Whenever an activity is clicked
	//Using .on instead of .click as these tags would be inserted dynamically after page load
	$('.section-activity-list .activities').on('click', 'div', (event) => {
		
		//clear card-list
		$(".card-list").html('');
		
		//Scroll to section to show other people
		$('html, body').animate({
			scrollTop: $(".section-other-people").offset().top
		}, 300);
		
		var selectedActivity=$(event.currentTarget).html();
		console.log("Deleting old entry of this activity for same user in Firebase!!");
		database.ref("/userActivities/"+selectedActivity+"/"+userId).remove()
		.then(()=>{
			console.log("Successfully deleted entry for User...");
			console.log("Adding new entry...");
			database.ref("/userActivities/"+selectedActivity+"/"+userId)
			.set({
				userName: userName, 
				profilePicURL: profilePicURL, 
				deviceId: "dummy", 
				screenName: screenName,
				location : currentLocation,
				startTime: new Date()
			  }
			)
			.then(() =>{
				console.log("Successfully added new entry for user in Firebase DB!!");
				console.log("Fetching all users who are performing same activity...");
				database.ref('/userActivities/'+selectedActivity)
				.once('value', (snapshot) => {
					let data = snapshot.val();
					console.log("Successfully Fecthed all users who are performing same activity !!");
					console.log("Users who are performing same activity:"+data);
					Object.keys(data).map(key =>{ 
						$(".card-list").append(
							"<a href='https://m.me/'"+data[key].screenName+" target='_blank'><div class='card'><div><img class='card-image' src='"+data[key].profilePicURL+"'></div><br><div>"+data[key].userName+"</div><div>"+data[key].location+"</div></div></a>"
						);
					})
					
				});
			})
		})
	});
	
});


