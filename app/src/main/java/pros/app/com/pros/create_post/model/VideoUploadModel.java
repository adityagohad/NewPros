package pros.app.com.pros.create_post.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoUploadModel {

    private String id;
    private String status;
    private String created_at;
    private String updated_at;
    private String mime_type;
    private String original_filename;
    private String source_url;
    private String duration;
    private String height;
    private String width;
    private String extname;
    private int file_size;
    private String video_bitrate;
    private String audio_bitrate;
    private String audio_codec;
    private String video_codec;
    private String fps;
    private String audio_channels;
    private String audio_sample_rate;
    private String path;
    private int encodings_count;


    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getMime_type() {
        return mime_type;
    }

    public String getOriginal_filename() {
        return original_filename;
    }

    public String getSource_url() {
        return source_url;
    }

    public String getDuration() {
        return duration;
    }

    public String getHeight() {
        return height;
    }

    public String getWidth() {
        return width;
    }

    public String getExtname() {
        return extname;
    }

    public int getFile_size() {
        return file_size;
    }

    public String getVideo_bitrate() {
        return video_bitrate;
    }

    public String getAudio_bitrate() {
        return audio_bitrate;
    }

    public String getAudio_codec() {
        return audio_codec;
    }

    public String getVideo_codec() {
        return video_codec;
    }

    public String getFps() {
        return fps;
    }

    public String getAudio_channels() {
        return audio_channels;
    }

    public String getAudio_sample_rate() {
        return audio_sample_rate;
    }

    public String getPath() {
        return path;
    }

    public int getEncodings_count() {
        return encodings_count;
    }
}