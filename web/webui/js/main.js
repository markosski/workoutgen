
define(function (require) {
	var $ = require('jquery');
	var mustache = require("mustache");
	var bootstrap = require("bootstrap");
	var multiselect = require("multiselect");

	$('[data-toggle="popover"]').popover({trigger: 'focus'});
	$('.selectpicker').selectpicker();

	function getWorkout() {
		var equip = ['bodyweight'];
		equip = equip.concat($('#workoutEquipment').val());

		var time = $('#workoutTime').val();
		var type = $('#workoutType').val();
		var exp = $('#workoutExperience').val();

		var url = 'http://localhost:8090/workout/' + type + '/' + time + '/' + exp + '/' + equip.join(',');
		var template = $('#workout-template').html();

		mustache.parse(template);

		$.getJSON(url, function(data) {
			var rendered = mustache.render(template, data.success);

			$('#workout-content').html(rendered);
		});
	};

   	$('.btn-submit').on('click', function(event) {
   		event.preventDefault();
   		getWorkout();
   		$('html, body').animate({
	        scrollTop: $("#workout-content").offset().top
	    }, 200);
   	});
});