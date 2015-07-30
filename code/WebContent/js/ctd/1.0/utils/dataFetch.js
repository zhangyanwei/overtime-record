define(function() {

// Private variable that is used to match array syntax in the data property object
var __reArray = /\[.*?\]$/;

/* If there is a . in the source string then the data source is in a 
 * nested object so we loop over the data for each level to get the next
 * level down. On each loop we test for undefined, and if found immediately
 * return. This allows entire objects to be missing and sDefaultContent to
 * be used if defined, rather than throwing an error
 */
var fetchData = function (data, src) {
	var a = src.split('.');
	var arrayNotation, out, innerSrc;

	if ( src !== "" )
	{
		for ( var i=0, iLen=a.length ; i<iLen ; i++ )
		{
			// Check if we are dealing with an array notation request
			arrayNotation = a[i].match(__reArray);

			if ( arrayNotation ) {
				a[i] = a[i].replace(__reArray, '');

				// Condition allows simply [] to be passed in
				if ( a[i] !== "" ) {
					data = data[ a[i] ];
				}
				out = [];
				
				// Get the remainder of the nested object to get
				a.splice( 0, i+1 );
				innerSrc = a.join('.');

				// Traverse each entry in the array getting the properties requested
				for ( var j=0, jLen=data.length ; j<jLen ; j++ ) {
					out.push( fetchData( data[j], innerSrc ) );
				}

				// If a string is given in between the array notation indicators, that
				// is used to join the strings together, otherwise an array is returned
				var join = arrayNotation[0].substring(1, arrayNotation[0].length-1);
				data = (join==="") ? out : out.join(join);

				// The inner call to fetchData has already traversed through the remainder
				// of the source requested, so we exit from the loop
				break;
			}

			if ( data === null || data[ a[i] ] === undefined )
			{
				return undefined;
			}
			data = data[ a[i] ];
		}
	}

	return data;
};

var util = {
	fetchData: fetchData
};

return util;

});

