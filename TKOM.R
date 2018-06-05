vector1 <- c(1, 2, 3)
vector2 <- c(3, 2, 1, 3, 2, 1)

print(2 * vector1)

matrix1 <- matrix(c(1, 5, 9, 2, 3, 4, 6, 7, 8), nrow = 3, ncol = 3, byrow = TRUE)
matrix(c(1, 5, 9, 2, 3, 4, 6, 7, 8), nrow = 3, ncol = 3, byrow = FALSE) -> matrix2

print(matrix1 * matrix2)
print(matrix1 %*% matrix2)

print(matrix1 + matrix2)

print (matrix1 / 3)

print(vector1[1])

if (det(matrix2)) {
  print("Yay!")
}

for (i in 1:10) {
  print(i)
}

x <- "10"
x <- 10
if (x)
	print("wow")

while (i < 20) {
  i = i + 1
  print(i)
}

repeat {
  i = i + 1
  if (i == 25)
    next
  print(i)
  if (i == 30)
    break
  xxxxxxd <- 33
}

repeat {
	xxx <- 3
	break
}
	
for (i in 1:10) {
	print(i)
	a <- i
}
	
while (i < 20) {
	i <- i + 1
	ddd <- i + 2
	}

text = "TKOM"

var1 = 10
var1 = "A teraz jestem stringiem"

matrix3 = matrix(c(1, 2, 3, 4), nrow = 4, ncol = 1, byrow = TRUE)
matrix4 <- matrix(c(1, 2, 3, 4), nrow = 2, ncol = 2)

len4 <- length(matrix4)

if (length(matrix3) == len4)
	matrix5 <- matrix(c(1, 2, 3, 4, 5), nrow = 5, ncol = 1, byrow = TRUE)
	
toCheck <- 1

{
	toCheck <- 2
	{
		toCheck <- 3
		{
			toCheck <- 4
			toAssign <- toCheck
		}
	}
}

boolean <- 1 && 0
