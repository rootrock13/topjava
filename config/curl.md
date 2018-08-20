The Examples of HTTP requests using Curl for REST controllers of topjava project (for Windows you can use Git Bash)
----------------------
>###1. User requests

<pre><b>1.1. Get user with id = 100000</b>
<i>curl http://localhost:8080//topjava/rest/admin/users/100000</i></pre>

<pre><b>1.2. Get all users</b>
<i>curl http://localhost:8080/topjava/rest/admin/users</i></pre>


>###2. Meal requests

<pre><b>2.1. Get meal with id = 100003</b>
<i>curl http://localhost:8080/topjava/rest/meals/100003</i></pre>

<pre><b>2.2. Get all meals</b>
<i>curl http://localhost:8080/topjava/rest/meals</i></pre>

<pre><b>2.3. Get all meals filtered by date/time</b>
<i>curl http://localhost:8080/topjava/rest/meals/between?fromDate=2015-05-31&fromTime=10:30</i>
Filter parameters: <i>fromDate, toDate, fromTime, toTime</i></pre>

<pre><b>2.4. Add new meal</b>
<i>curl -X POST -d '{"dateTime":"2018-08-20T13:00","description":"new meal","calories":800}' -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/topjava/rest/meals</i></pre>

<pre><b>2.5. Update meal with id = 100002</b>
<i>curl -X PUT -d '{"dateTime":"2018-08-20T13:10","description":"updated meal","calories":800}' -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/topjava/rest/meals/100002</i></pre>

<pre><b>2.6. Delete meal with id = 100002</b>
<i>curl -X DELETE http://localhost:8080/topjava/rest/meals/100002</i></pre>
