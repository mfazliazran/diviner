Diviner can represent application behaviors in different methods;
One of these methods is conversion into a source code representation of the application logic and behaviors.

The following rules are currently used to convert behaviors into server-side pseudo code:



&lt;TABLE border="1" cellspacing="0"&gt;




&lt;TR&gt;




&lt;TD&gt;



&lt;B&gt;

Plugin

&lt;/B&gt;



&lt;/TD&gt;




&lt;TD&gt;



&lt;B&gt;

Behavior

&lt;/B&gt;



&lt;/TD&gt;




&lt;TD&gt;



&lt;B&gt;

Code

&lt;/B&gt;



&lt;/TD&gt;




&lt;/TR&gt;




&lt;TR&gt;




&lt;TD valign="top"&gt;

Output / Reflection

&lt;/TD&gt;




&lt;TD valign="top"&gt;

Input reflected in output (direct)

&lt;/TD&gt;




&lt;TD&gt;



&lt;U&gt;

JSP

&lt;/U&gt;



&lt;BR/&gt;



&lt;FONT size="2"&gt;

Source Page

&lt;/FONT&gt;





&lt;TABLE border="1" cellspacing="0"&gt;




&lt;TR&gt;



&lt;TD&gt;




&lt;FONT size="2"&gt;


String input{id} = request.getParameter("{id}");

&lt;BR/&gt;


...

&lt;BR/&gt;


out.println (input{id});


&lt;/FONT&gt;




&lt;/TD&gt;



&lt;/TR&gt;




&lt;/TABLE&gt;





&lt;/TD&gt;




&lt;/TR&gt;




&lt;/TABLE&gt;






&lt;TBD&gt;

