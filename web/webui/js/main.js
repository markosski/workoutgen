
define(function (require) {
	var $ = require('jquery');
	var mustache = require("mustache");
	var bootstrap = require("bootstrap");
	var multiselect = require("multiselect");

	$('[data-toggle="popover"]').popover({trigger: 'focus'});
	$('[data-toggle="tooltip"]').tooltip();
	$('.selectpicker').selectpicker();

	function getWorkout() {
		var equip = ['bodyweight'];
		equip = equip.concat($('#workoutEquipment').val());

		var time = $('#workoutTime').val();
		var type = $('#workoutType').val();
		var exp = $('#workoutExperience').val();

		var url = 'http://34.230.27.92:8090/workout/' + type + '/' + time + '/' + exp + '/' + equip.join(',');
		var template = $('#workout-template').html();

		mustache.parse(template);
		// $('#workout-content').html('<p>Loading...</p>');
		$.getJSON(url, function(data) {
			var rendered = mustache.render(template, data.success);

			$('#workout-content').hide();
			$('#workout-content').html(rendered);
			$('#workout-content').fadeIn();
		});
	};

   	$('.btn-submit').on('click', function(event) {
   		event.preventDefault();
   		getWorkout();
   		// $('html, body').animate({
	    //     scrollTop: $("#workout-content").offset().top
	    // }, 200);
   	});
});