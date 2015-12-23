$(document).ready(function() {
		
		$("#qtnSubmit").click(function() {
			var aid = alphanumeric_unique();
			var Text = $("#answer").val();
		    var Timestamp = new Date().toISOString();


			location.href='middleServlet';
			$.ajax({
				  type: "POST",
				  url: 'answer',
				  data: {"aId":aid,
					  	 "text":Text,
					  	 "time":Timestamp
					  	},
				  success: function(result) {
				    }
				});
		});		
		
	});

	function alphanumeric_unique() {
	    return Math.random().toString(36).split('').filter( function(value, index, self) { 
	        return self.indexOf(value) === index;
	    }).join('').substr(2,8);
	}