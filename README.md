## 图片压缩
### 通过源文件路径
```java
compressImg(String sourceFilePath, Float quality, Integer width, Integer height);
```
- sourceFilePath是源文件路径 required
- quality是压缩的质量，值在0到1之间
- width是压缩后的width
- height是压缩后的height

### 通过源文件
```java
compressImg(File sourceFile, Float quality, Integer width, Integer height);
```
- sourceFile是源文件 required

### 通过源文件流和文件类型
```java
compressImg(InputStream srouceInput, String formatName, Float quality, Integer width, Integer height);
```
- srouceInput是源文件流 required
- formatName是源文件后缀 required

## 其他文件压缩
待完善
