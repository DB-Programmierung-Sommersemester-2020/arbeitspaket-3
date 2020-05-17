"using strict";

window.onload = function()
{
	getNotes();
}

function logout()
{
	fetch('Logout',{method: 'post'})
    .catch( error => console.error('Error:', error) );
}

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Functions accessing NoteController
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

function getNotes()
{
	fetch('Notes')
	.then( response => response.json() )
	.then( data => printListe(data) )
	.catch( error => console.error('Error:', error) );
}

function clearNotes()
{
	fetch('Notes', { method: 'delete'})
	 .then( response => fetch('Notes') )
	 .then( response => response.json() )
	 .then( data => printListe(data) )
	 .catch( error => console.error('Error:', error) );
}

function addNote()
{
	let data = { subject : document.querySelector("#subject").value, 
			     content : document.querySelector("#content").value 
			   };

	fetch('Notes',   {
	    method: 'post',
	    headers: {
	    	'Content-type': 'application/x-www-form-urlencoded' 
	    },
	    body: 'subject='+data.subject+'&content=' + data.content
	  })
	.then( response => fetch('Notes') )
	.then( response => response.json() )
	.then( data => printListe(data) )
	.catch( error => console.error('Error:', error) );
}



function printListe(data)
{
	document.querySelector("#subject").value = "";
	document.querySelector("#content").value = "";
	
	let div = document.querySelector("#notelist");
	div.innerHTML = "";
	let ul = document.createElement("ul");
	div.append( ul );
		
	data.forEach( item => { 
		let li = document.createElement("li");
		li.append( item.creationDate + " : " + item.subject  + " => " + item.content );
		ul.append(li);
	} );
}
