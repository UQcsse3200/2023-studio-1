Xvfb :1 & # Create an X server to a virtual framebuffer
XVFB_PID=$!

DISPLAY=:1 ./gradlew clean run 2>game.log 1>gradle.log &
GAME_PID=$!

sleep 35 # Give the game some time to launch

# Click 640,417 to start new game and then 640,790 to skip the intro sequence
DISPLAY=:1 xdotool mousemove 640 417 click 1 # Click 'New Game' 

sleep 10
DISPLAY=:1 xdotool mousemove 640 790 click 1 # Click 'Continue'

sleep 35
DISPLAY=:1 xwd -root -out game_launched.xwd

# Clean up 
pkill -P $$

# Check if run was successful
grep Exception game.log
if [ $? -eq 1 ]; then
  echo "Run failed. Last 30 lines of log:"
  tail -30 game.log
  OUTPUT_VAL=1
else
  OUTPUT_VAL=0
fi

rm game.log gradle.log
exit ${OUTPUT_VAL}
