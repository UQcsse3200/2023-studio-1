Xvfb :1 & # Create an X server to a virtual framebuffer
XVFB_PID=$!

DISPLAY=:1 ./gradlew clean run 2>game.log 1>gradle.log &
GAME_PID=$!

sleep 35 # Give the game some time to launch

# Click 640,417 to start new game and then 640,790 to skip the intro sequence
DISPLAY=:1 xdotool mousemove 640 380 click 1 # Click 'New Game'

sleep 10
DISPLAY=:1 xdotool mousemove 640 790 click 1 # Click 'Continue'

sleep 35
DISPLAY=:1 xwd -root -out game_launched.xwd

# Clean up 
pkill -P $$

# Check if run was successful
grep Exception game.log
if [ $? -eq 0 ]; then
  echo "Run failed. If this is unexpected, see https://github.com/UQcsse3200/2023-studio-1/wiki/Launch-Testing#my-commits-fail-this-test-and-i-dont-know-why"
  echo "Last 30 lines of log:"
  tail -30 game.log
  OUTPUT_VAL=1
else
  OUTPUT_VAL=0
fi

rm game.log gradle.log
exit ${OUTPUT_VAL}
