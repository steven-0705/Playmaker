addr="http://localhost:8080"

echo "output will be sent to output.log"
rm output.log
touch output.log


echo "creating users..."

curl "$addr/users?user_id=test1&user_name=chris&user_email=chrisf671@gmail.com" >> output.log
curl "$addr/users?user_id=test2&user_name=rick&user_email=rick@gmail.com" >> output.log
curl "$addr/users?user_id=test3&user_name=timmy&user_email=timmy@gmail.com" >> output.log
curl "$addr/users?user_id=test4&user_name=joe&user_email=joe@gmail.com" >> output.log
curl "$addr/users?user_id=test5&user_name=flapper&user_email=flapper@gmail.com" >> output.log


echo "creating a group..."

resp=`curl --data "" "$addr/groups?user_id=test1&group_name=FOOTBAAAAAAAAAALL"`
#group_id=`cat resp | grep '{"id":' | grep -Po "(?<=^id ).*"`
#echo "group id = $group_id"



echo "add the users to the group..."

curl --data "" "$addr/users?user_id=test2&group_id=1" >> output.log
curl --data "" "$addr/users?user_id=test3&group_id=1" >> output.log
curl --data "" "$addr/users?user_id=test4&group_id=1" >> output.log
curl --data "" "$addr/users?user_id=test5&group_id=1" >> output.log

echo "create event..."

curl --data "" "$addr/groups/events?user_id=test1&group_id=1&event_name=event&event_type=unknown&event_date=5" >> output.log

echo "add some users to event..."
curl --data "" "$addr/groups/events?group_id=1&event_id=2&user_id=test3" >> output.log
curl --data "" "$addr/groups/events?group_id=1&event_id=2&user_id=test2" >> output.log
curl --data "" "$addr/groups/events?group_id=1&event_id=2&user_id=test4" >> output.log



