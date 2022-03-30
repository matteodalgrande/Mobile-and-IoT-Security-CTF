if (Java.available) {
	
	Java.perform(function() {
		
		// string split
		var splitString = Java.use('java.lang.String');
		splitString.split.overload("java.lang.String").implementation = function (regex) {
			
			// original split function call
			var result = this.split.call(this,regex);			
			
			// print output
			var args = [];
			var details = [];			
			for (var i = 0; i < result.length; i++) {
				details.push({'name': 'result', 'value': result[i]});
				var send_message = {
					'method': 'java.lang.String.split',
					'args': args,
					'details': details
				};
				send(send_message);
			}
			
			// return
			return result;
		}
		
		// traduction
		var TraductionFactory = Java.use("com.elokence.limuleapi.TraductionFactory");
		TraductionFactory.getTraductionFromToken.overload("java.lang.String").implementation = function (traduc) {
			
			// original getTraductionFromToken function call
			var result = this.getTraductionFromToken.call(this,traduc);
			
			// print output
			var args = [];
			var details = [];
			details.push({'name': 'traductionInput', 'value': traduc});
			details.push({'name': 'traductionOutput', 'value': result});		
			var send_message = {
				'method': 'com.elokence.limuleapi.TraductionFactory.getTraductionFromToken',
				'args': args,
				'details': details
			};
			send(send_message);
			
			// hijack value
			result = "hackinator";
			
			// return
			return result;
		}
		
	});

}