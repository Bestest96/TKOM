5.5 -> xd
xd <- 3
vector1 <- c(1, 2, 3)
vector2 <- c(3, 2, 1)

vectorSum = vector1 + vector2 #c(4, 4, 4, 4, 4, 4)

print(length(vectorSum))

x <- seq(1, 10)

print(2 * vector1)

matrix1 <- matrix(c(1, 5, 9, 2, 3, 4, 6, 7, 8), nrow = 3, ncol = 3, byrow = TRUE)
matrix(c(1, 5, 9, 2, 3, 4, 6, 7, 8), nrow = 3, ncol = 3, byrow = FALSE) -> matrix2

print(matrix1 + matrix2)

print(vector1[1])

tryCatch({
	hahah <- 3
	hahahb <- 0
	print (hahah / hahahb)
},
         error = function(e) {
           print(e)
         })

haha <- length(vectorSum)

if (length(vectorSum) == 3)
	print("Nice!")

for (i in 1:10) {
  print(i)
}

a = 1
a -> b

while (i < 20) {
  i = i + b
  print(i)
}

repeat {
  i = i + a
  if (i == 25)
    next
  print(i)
  if (i == 30)
    break
}

text = "TKOM"

switch (text,
  "TKOM" = print("Projekt TKOM"),
  "TIN" = print("Projekt TIN"),
  print("Projekt nieznany")
)


# Halo halo
# jestem komentarzem w R


# Możliwe dodatkowe funkcjonalności
var1 = 10
var1 = "A teraz jestem stringiem"

matrix3 = matrix(c(1, 2, 3, 4), nrow = 4, ncol = 1, byrow = TRUE)
tryCatch(10 / 0,
         error = function(e) {
           print(e)
         })
		 