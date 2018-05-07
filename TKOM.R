xd <- 3
vector1 <- c(1, 2, 3)
vector2 <- c(3, 2, 1, 3, 2, 1)
vector3 = c("text1", 3.2, "text3") # vector of characters

vectorSum = vector1 + vector2 #c(4, 4, 4, 4, 4, 4)

print(length(vectorSum))

print(2 * vector1)

matrix1 <- matrix(c(1, 5, 9, 2, 3, 4, 6, 7, 8), nrow = 3, ncol = 3, byrow = TRUE)
matrix(c(1, 5, 9, 2, 3, 4, 6, 7, 8), nrow = 3, ncol = 3, byrow = FALSE) -> matrix2

print(det(matrix1))

print(matrix1 + matrix2)

print(vector1[1])

if (det(matrix1) == 0) {
  print("Nie mam macierzy odwrotnej")
}

for (i in 1:10) {
  print(i)
}

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
tryCatch(det(matrix3),
         error = function(e) {
           print(e)
         })
		 