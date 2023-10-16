Xvfb :1 & # Create an X server to a virtual framebuffer
XVFB_PID=$!

DISPLAY=:1 ./gradlew clean run 2>game.log 1>gradle.log &
GAME_PID=$!

sleep 60 # Give the game some time to launch

# Click 640,380 to start new game and then 640,790 to skip the intro sequence
DISPLAY=:1 xdotool mousemove 640 380 click 1 # Click 'New Game'

sleep 10
DISPLAY=:1 xdotool mousemove 640 750 click 1 # Click 'Continue'

sleep 12

DISPLAY=:1 xdotool mousemove 640 512 click 1 # Click 'Awaken'

sleep 35

DISPLAY=:1 xwd -root -out game_launched.xwd

# Clean up 
pkill -P $$

# Start by assuming run passed
OUTPUT_VAL=0

# Check if run threw exceptions
grep Exception game.log > /dev/null
if [ $? -eq 0 ]; then
  echo "Run failed: Exception thrown."
  echo "If this is unexpected, see https://github.com/UQcsse3200/2023-studio-1/wiki/Launch-Testing#my-commits-fail-this-test-and-i-dont-know-why"
  echo "Last 30 lines of log:"
  tail -30 game.log
  OUTPUT_VAL=1
fi

# Check that the run actually made it to the game screen
grep MAIN_GAME game.log > /dev/null
if [ $? -eq 1 ]; then
  # Not found, e.g.
  echo "Run failed: did not succesfully enter game"
  OUTPUT_VAL=1
fi

# Future improvements: verify that the game fully loads, not just the main-game was selected.

exit ${OUTPUT_VAL}
