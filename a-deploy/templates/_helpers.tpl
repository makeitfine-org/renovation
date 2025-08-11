{{- define "ca.name" -}}
{{ .Values.name }}
{{- end }}


{{- define "ca.metadata" -}}
metadata:
  name: {{ include "ca.name" . }}
  namespace: {{ .Values.namespaceOverride | default .Release.Namespace }}
{{- end }}


{{- define "ca.fullImage" -}}
{{ .Values.image.repository }}:{{ .Values.image.tag }}
{{- end }}


{{- define "ca.imageAndEnv" -}}
- name: {{ include "ca.name" . | quote }}
  image: {{ include "ca.fullImage" . | quote }}
  imagePullPolicy: {{ .Values.image.pullPolicy | quote }}
  envFrom:
    - configMapRef:
        name: {{ .Values.global.parentChartName }}-configmap
    - secretRef:
        name: {{ .Values.global.parentChartName }}-secret
{{- end }}


{{/*metadata:*/}}
{{/*  name: {{ include "mychart.fullname" . }}*/}}

{{ define "mychart.fullname" -}}
{{- printf "%s-%s" .Release.Name .Chart.Name | trunc 63 | trimSuffix "-" -}}
{{- end -}}


{{- define "mychart.fullImage" -}}
{{ .Values.image.repository }}:{{ .Values.image.tag }}
{{- end -}}


{{/*metadata:*/}}
{{/*  labels:*/}}
{{/*    {{ include "mychart.labels" . | nindent 4 }}*/}}

{{- define "mychart.labels" -}}
app.kubernetes.io/name: {{ .Chart.Name }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/version: {{ .Chart.AppVersion }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}


{{/*spec:*/}}
{{/*  selector:*/}}
{{/*    matchLabels:*/}}
{{/*      {{ include "mychart.selectorLabels" . | nindent 6 }}*/}}

{{- define "mychart.selectorLabels" -}}
app.kubernetes.io/name: {{ .Chart.Name }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}


{{/*spec:*/}}
{{/*  containers:*/}}
{{/*    - name: {{ .Chart.Name }}*/}}
{{/*      image: {{ include "mychart.image" . }}*/}}

{{- define "mychart.image" -}}
{{ .Values.image.repository }}:{{ default .Chart.AppVersion .Values.image.tag }}
{{- end -}}


{{/*Helm will render the ConfigMap,*/}}
{{/*metadata:*/}}
{{/*  labels:*/}}
{{/*    {{ include "annotation.reread.configmap" . | nindent 2 }}*/}}

{{- define "annotation.reread.configmap" -}}
annotations:
  checksum/config: {{ include (print $.Template.BasePath "/configmap.yaml") . | sha256sum }}
{{- end -}}





