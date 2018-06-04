vector1 <- c(1, 2, 3)
vector2 <- c(3, 2, 1, 3, 2, 1)

print(2 * vector1)

matrix1 <- matrix(c(1, 5, 9, 2, 3, 4, 6, 7, 8), nrow = 3, ncol = 3, byrow = TRUE)
matrix(c(1, 5, 9, 2, 3, 4, 6, 7, 8), nrow = 3, ncol = 3, byrow = FALSE) -> matrix2

print(matrix1 + matrix2)

print (matrix1 / 3)

print(vector1[1])

if (length(vector1) == 3) {
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
}

text = "TKOM"

switch (text,
  "TKOM" = {
	  print("Projekt TKOM")
	},
  "TIN" = {
	  print("Projekt TIN")
    },
		{
		  print("Projekt nieznany")
	}
)

var1 = 10
var1 = "A teraz jestem stringiem"

matrix3 = matrix(c(1, 2, 3, 4), nrow = 4, ncol = 1, byrow = TRUE)
matrix4 <- matrix(c(1, 2, 3, 4), nrow = 2, ncol = 2)
tryCatch(print(matrix3 %*% matrix4),
         error = function(e) {
           print(e)
         })
		 