# importing the required module
import matplotlib.pyplot as plt
  
# x axis values
x = [4984, 5245, 7091, 1194, 1252, 646, 1085,1149, 1441]
# plotting the points 
plt.plot(x)
  
# naming the x axis
plt.xlabel('request number')
# naming the y axis
plt.ylabel('time')
  
# giving a title to my graph
plt.title('Time graph for different requests')
  
# function to show the plot
plt.savefig("./assets/plot.png", dpi=400)